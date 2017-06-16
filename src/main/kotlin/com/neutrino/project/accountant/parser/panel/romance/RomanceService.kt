package com.neutrino.project.accountant.parser.panel.romance

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.ParserService
import com.neutrino.project.accountant.parser.form.LoginForm
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.util.ClientUtil
import com.neutrino.project.accountant.util.exception.AuthException
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import java.time.LocalDate

class RomanceService(val client: ReactiveClient, val dateRange: Pair<LocalDate, LocalDate>): ParserService {

    private val logger = LogManager.getLogger(this)

    private val httpController = RomanceHttpController(client)
    private val listener = RomanceResponseListener()

    private val profileHandler = RomanceProfileHandler(httpController)
    private val statisticHandler = RomanceStatisticHandler(httpController)

    override fun auth(credential: LoginForm) {
        logger.info("auth")
        httpController.requestAndSubscribe(credential.get(), listener)
    }

    override fun profilesImport(): Flux<Profile> {
        logger.info("profilesImport")
        return profileHandler.handle(Unit)
    }

    override fun statistics(): Flux<Statistic> {
        logger.info("statistics")
        return statisticHandler.handle(dateRange)
                .map { ClientUtil.converStatistic(it, client.name()) }
    }

    inner class RomanceResponseListener: ResponseListener {

        @Throws(AuthException::class, BadCredentialException::class)
        override fun success(response: Response) {
            logger.info(response)
            val page = ClientUtil.stringBodyAndClose(response)

            if (page!!.contains("Access denied! Please, log in")) {
                throw AuthException()
            }

            if (page.contains("Login or password is incorrect")) {
                throw BadCredentialException()
            }
        }

        override fun error(e: Throwable) {
            logger.error("Error in request time", e)
        }
    }
}
