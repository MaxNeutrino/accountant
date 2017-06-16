package com.neutrino.project.accountant.parser.panel.chatos

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.ParserService
import com.neutrino.project.accountant.parser.form.LoginForm
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.parser.panel.chatos.support.anastasia.ChatOsAnastasiaStatisticHandler
import com.neutrino.project.accountant.util.ClientUtil
import com.neutrino.project.accountant.util.exception.AuthException
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import java.time.LocalDate


class ChatOsService(private val client: ReactiveClient, private val dateRange: Pair<LocalDate, LocalDate>): ParserService {

    private val logger = LogManager.getLogger(this)

    private val listener = ChatOsResponseListener()
    private val httpController = ChatOsHttpController(client)
    private val profileHandler = ChatOsProfileParserHandler(httpController)

    private val anastasiaStatisticHandler = ChatOsAnastasiaStatisticHandler(client)

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
        return anastasiaStatisticHandler.handle(dateRange)
    }

    inner class ChatOsResponseListener: ResponseListener {

        override fun success(response: Response) {
            logger.info(response)
            val page = ClientUtil.stringBodyAndClose(response)

            if (page!!.contains("Для входа в личный кабинет, пожалуйста авторизируйтесь")) {
                throw AuthException()
            }

            if (page.contains("Неверный Email или пароль")) {
                throw BadCredentialException()
            }
        }

        override fun error(e: Throwable) {
            logger.error("Error in request time", e)
        }
    }
}