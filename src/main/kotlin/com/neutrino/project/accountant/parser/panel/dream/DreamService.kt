package com.neutrino.project.accountant.parser.panel.dream

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


class DreamService(private val client: ReactiveClient, private val dateRange: Pair<LocalDate, LocalDate>): ParserService {

    private val logger = LogManager.getLogger(this)

    private val httpController = DreamHttpController(client)
    private val listener = DreamResponseListener()

    private val profileHandler = DreamProfileHandler(httpController)
    private val statisticHandler = DreamStatisticHandler(httpController)

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
                .map {
                    ClientUtil.converStatistic(it, client.name())
                }
    }

    inner class DreamResponseListener : ResponseListener {

        override fun success(response: Response) {
            logger.info(response)
            val page = ClientUtil.stringBodyAndClose(response)

            if (page!!.contains("Неправильная комбинация логин")) {
                throw BadCredentialException()
            }

            if (page.contains("Введите Ваш логин и пароль")) {
                throw AuthException()
            }
        }

        override fun error(e: Throwable) {
            logger.error("Error in request time", e)
        }
    }
}