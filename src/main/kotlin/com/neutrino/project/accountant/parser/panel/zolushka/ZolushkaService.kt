package com.neutrino.project.accountant.parser.panel.zolushka

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.ParserService
import com.neutrino.project.accountant.parser.form.AbstractLoginForm
import com.neutrino.project.accountant.parser.form.LoginForm
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.util.ClientUtil
import com.neutrino.project.accountant.util.exception.AuthException
import com.neutrino.project.accountant.util.exception.BadCredentialException
import com.pushtorefresh.javac_warning_annotation.Warning
import okhttp3.Response
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


class ZolushkaService(private val client: ReactiveClient, private val dateRange: Pair<LocalDate, LocalDate>): ParserService {

    private val logger = LogManager.getLogger(this)

    private var isLastLoggedIn: Boolean = false
    private val listener = ZolushkaResponseListener()
    private val httpController = ZolushkaHttpController(client)

    private val statisticHandler = ZolushkaStatisticHandler(httpController, Pair(1.0, 1.0))

    override fun auth(credential: LoginForm) {
        logger.info("auth")
        isLastLoggedIn = false
        httpController.requestAndSubscribe(credential.get(), listener)
    }

    @Warning("Return empty")
    override fun profilesImport(): Flux<Profile> {
        logger.info("profilesImport")
        return Flux.empty()
    }

    override fun statistics(): Flux<Statistic> {
        logger.info("statistics")
        return statisticHandler.handle(dateRange)
                .map { ClientUtil.converStatistic(it, client.name()) }
    }

    private fun lastRequestSecureLogin() {
        client.get("/securelogin/login.aspx")
                .subscribe(
                        {r -> listener.success(r)},
                        {e -> listener.error(e)}
                )
    }

    private fun generateTermsLoginData(page: String): Mono<TermsLoginForm> {
        return ZolushkaHiddenFormParser()
                .parse(page)
                .map { TermsLoginForm(it) }
    }

    inner class ZolushkaResponseListener: ResponseListener {

        override fun success(response: Response) {
            logger.info(response)
            val page = ClientUtil.stringBodyAndClose(response)

            if (page!!.contains("Username/Password is incorrect"))
                throw BadCredentialException()

            if (page.contains("Login_Form"))
                throw AuthException()

            if (page.contains("Cooperation Agreement")) {
                generateTermsLoginData(page)
                        .subscribe { httpController.requestAndSubscribeTerms(it.get(), this) }
            }

            if (page.contains("You do not appear to be logged in"))
                throw AuthException()

            if (!isLastLoggedIn) {
                isLastLoggedIn = true
                lastRequestSecureLogin()
            }
        }

        override fun error(e: Throwable) {
            logger.error("Error in request time", e)
        }
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