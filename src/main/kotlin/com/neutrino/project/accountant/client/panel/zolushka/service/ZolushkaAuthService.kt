package com.neutrino.project.accountant.client.panel.zolushka.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.form.AbstractLoginForm
import com.neutrino.project.accountant.client.form.LoginForm
import com.neutrino.project.accountant.client.listener.ResponseListener
import com.neutrino.project.accountant.client.panel.zolushka.http.ZolushkaAuthHttp
import com.neutrino.project.accountant.client.panel.zolushka.http.ZolushkaAuthTermsHttp
import com.neutrino.project.accountant.client.panel.zolushka.parser.ZolushkaHiddenFormParser
import com.neutrino.project.accountant.client.service.AuthService
import com.neutrino.project.accountant.client.util.ClientUtil
import com.neutrino.project.accountant.util.exception.AuthException
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response
import reactor.core.publisher.Mono


class ZolushkaAuthService(val client: ReactiveClient) : AuthService, ResponseListener {

    val authHttp = ZolushkaAuthHttp(client)
    val termsAuthHttp = ZolushkaAuthTermsHttp(client)

    private var isLastLoggedIn: Boolean = false

    override fun auth(credential: LoginForm) {
        isLastLoggedIn = false
        authHttp.auth(credential.get(), this)
    }

    override fun success(response: Response) {
        val page = ClientUtil.stringBodyAndClose(response)

        if (page!!.contains("Username/Password is incorrect"))
            throw BadCredentialException()

        if (page.contains("Login_Form"))
            throw AuthException()

        if (page.contains("Cooperation Agreement")) {
            generateTermsLoginData(page)
                    .subscribe { termsAuthHttp.auth(it.get(), this) }
        }

        if (page.contains("You do not appear to be logged in"))
            throw AuthException()

        if (!isLastLoggedIn) {
            isLastLoggedIn = true
            lastRequestSecureLogin()
        }
    }

    override fun error(e: Throwable) {
        e.printStackTrace()
    }

    private fun lastRequestSecureLogin() {
        client.get("/securelogin/login.aspx")
                .subscribe(
                        {r -> this.success(r)},
                        {e -> this.error(e)}
                )
    }

    private fun generateTermsLoginData(page: String): Mono<TermsLoginForm> {
        return ZolushkaHiddenFormParser()
                .parse(page)
                .map { TermsLoginForm(it) }
    }

    inner class TermsLoginForm(data: Map<String, String>) : AbstractLoginForm() {

        init {
            load(data)
            custom("uxAgree", "I ACCEPT")
        }

        @Throws(UnsupportedOperationException::class)
        override fun login(login: String) {
            throw UnsupportedOperationException()
        }

        @Throws(UnsupportedOperationException::class)
        override fun password(password: String) {
            throw UnsupportedOperationException()
        }

        fun load(data: Map<String, String>) {
            credential.putAll(data)
        }
    }
}