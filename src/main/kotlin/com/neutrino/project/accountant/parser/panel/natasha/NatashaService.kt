package com.neutrino.project.accountant.parser.panel.natasha

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.client.cookie.CookieHandler
import com.neutrino.project.accountant.parser.ParserService
import com.neutrino.project.accountant.parser.database.ProfileStore
import com.neutrino.project.accountant.parser.form.LoginForm
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.parser.panel.natasha.form.NatashaCodeLoginForm
import com.neutrino.project.accountant.parser.panel.natasha.form.NatashaLastAutoGenLoginForm
import com.neutrino.project.accountant.parser.panel.natasha.form.NatashaLoginForm
import com.neutrino.project.accountant.util.ClientUtil
import com.neutrino.project.accountant.util.exception.AuthException
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response
import reactor.core.publisher.Flux
import java.time.LocalDate


class NatashaService(private val client: ReactiveClient, val dateRange: Pair<LocalDate, LocalDate>): ParserService {

    private var cookieFlag: Boolean = false

    private val cookieHandler = CookieHandler()

    private val listener = NatashaResponseListener()

    private var httpController = NatashaHttpController(client)

    private val authHandler = AuthHandler()

    private val profileHandler = NatashaProfileHandler(httpController)
    private val statisticHandler = NatashaStatisticHandler(httpController, dateRange)

    /**
     * Auth method
     * After throws AuthException need get service as NatashaService and call #code(String) method
     *
     * @throws AuthException
     * @throws BadCredentialException
     */
    @Throws(AuthException::class, BadCredentialException::class)
    override fun auth(credential: LoginForm) {
        if (!cookieFlag) {
            if (cookieCheck()) {
                auth(credential.get())
            }
        } else {
            auth(credential.get())
        }
    }

    override fun profilesImport(): Flux<Profile> {
        return profileHandler.handle(Unit)
    }

    override fun statistics(): Flux<Statistic> {
        val profiles = ProfileStore.getBySite(Site.NATASHA)
        return statisticHandler.handle(profiles)
                .map { ClientUtil.converStatistic(it, client.name()) }
    }

    fun code(code: String) {
        authHandler.code(code)
    }

    @Throws(AuthException::class, BadCredentialException::class)
    private fun auth(data: Map<String, String>) {
        httpController.requestAndSubscribe(data, listener)
    }

    private fun cookieCheck(): Boolean {
        cookieFlag = true

        cookieHandler.loadCookie(client.client)

        val page = httpController.request("/index.php").block()

        return page
                .contains("need to login before you can use this page")
    }


    private fun check(page: String): Boolean = page.contains(
            "Login filed")

    inner class NatashaResponseListener: ResponseListener {

        @Throws(AuthException::class, BadCredentialException::class)
        override fun success(response: Response) {
            val page = ClientUtil.stringBodyAndClose(response)

            if (!authHandler.last) {
                if (page!!.contains("Code")) {
                    throw AuthException()
                } else {
                    authHandler.last(page)
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
    }

    inner class AuthHandler {

        var last = false

        private var form: LoginForm? = null

        private val temp = httpController

        @Throws(AuthException::class)
        fun auth(login: String, password: String) {
            form = NatashaLoginForm(login, password)
            auth(form!!)
        }

        fun code(code: String) {
            httpController = NatashaNextAuthHttpController(httpController)
            val codeForm = NatashaCodeLoginForm(form!!, code)
            auth(codeForm)
        }

        fun last(response: String) {
            last = true
            val genForm = NatashaLastAutoGenLoginForm(response)
            auth(genForm)
            httpController = temp
        }
    }
}