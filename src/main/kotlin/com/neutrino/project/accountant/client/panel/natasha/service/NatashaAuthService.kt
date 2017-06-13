package com.neutrino.project.accountant.client.panel.natasha.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.cookie.CookieHandler
import com.neutrino.project.accountant.client.form.LoginForm
import com.neutrino.project.accountant.client.http.AuthHttp
import com.neutrino.project.accountant.client.listener.ResponseListener
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.panel.natasha.form.NatashaCodeLoginForm
import com.neutrino.project.accountant.client.panel.natasha.form.NatashaLastAutoGenLoginForm
import com.neutrino.project.accountant.client.panel.natasha.form.NatashaLoginForm
import com.neutrino.project.accountant.client.panel.natasha.http.auth.NatashaAuthHttp
import com.neutrino.project.accountant.client.panel.natasha.http.auth.NatashaNextAuth
import com.neutrino.project.accountant.client.service.AuthService
import com.neutrino.project.accountant.client.util.ClientUtil
import com.neutrino.project.accountant.util.exception.AuthException
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response


class NatashaAuthService(var client: ReactiveClient) : AuthService, ResponseListener {

    private var cookieFlag: Boolean = false

    private val cookieHandler: CookieHandler = CookieHandler()

    private var http: AuthHttp = NatashaAuthHttp(client)

    val handler = AuthHandler()

    override fun auth(credential: LoginForm) {
        if (!cookieFlag) {
            if (cookieCheck()) {
                auth(credential.get())
            }
        } else {
            auth(credential.get())
        }
    }

    override fun success(response: Response) {
        val page = ClientUtil.stringBodyAndClose(response)

        if (!handler.last) {
            if (page!!.contains("Code")) {
                throw AuthException()
            } else {
                handler.last(page)
            }
        } else {
            if (check(page!!))
                throw BadCredentialException()
            else {
                cookieHandler.saveCookie(client.client, Site.NATASHA.baseUrl + "/index.php")
            }
        }
    }

    override fun error(e: Throwable) {
        e.printStackTrace()
    }

    private fun auth(data: Map<String, String>) {
        http.auth(data, this)
    }

    private fun cookieCheck(): Boolean {
        cookieFlag = true

        cookieHandler.loadCookie(client.client)

        val checker = CheckRequest()

        val page = checker.send(client)!!
        return page
                .contains("need to login before you can use this page")
    }


    private fun check(page: String): Boolean = page.contains(
            "Login filed")

    private inner class CheckRequest {

        fun send(client: ReactiveClient): String? {
            val response: Response = client
                    .get("/index.php")
                    .block()

            return ClientUtil.stringBodyAndClose(response)
        }
    }

    inner class AuthHandler {

        var last = false

        private var form: LoginForm? = null

        private val temp = http

        @Throws(AuthException::class)
        fun auth(login: String, password: String) {
            form = NatashaLoginForm(login, password)
            auth(form!!)
        }

        fun code(code: String) {
            http = NatashaNextAuth(client)
            val codeForm = NatashaCodeLoginForm(form!!, code)
            auth(codeForm)
        }

        fun last(response: String) {
            last = true
            val genForm = NatashaLastAutoGenLoginForm(response)
            auth(genForm)
            http = temp
        }
    }
}