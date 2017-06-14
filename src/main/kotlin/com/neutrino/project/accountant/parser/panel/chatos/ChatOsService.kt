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
import reactor.core.publisher.Flux
import java.time.LocalDate


class ChatOsService(private val client: ReactiveClient, private val dateRange: Pair<LocalDate, LocalDate>): ParserService {

    private val listener = ChatOsResponseListener()
    private val httpController = ChatOsHttpController(client)
    private val profileHandler = ChatOsProfileParserHandler(httpController)

    private val anastasiaStatisticHandler = ChatOsAnastasiaStatisticHandler(client)

    override fun auth(credential: LoginForm) {
        httpController.requestAndSubscribe(credential.get(), listener)
    }

    override fun profilesImport(): Flux<Profile> {
        return profileHandler.handle(Unit)
    }

    override fun statistics(): Flux<Statistic> {
        return anastasiaStatisticHandler.handle(dateRange)
    }

    inner class ChatOsResponseListener: ResponseListener {

        override fun success(response: Response) {
            val page = ClientUtil.stringBodyAndClose(response)

            if (page!!.contains("Для входа в личный кабинет, пожалуйста авторизируйтесь")) {
                throw AuthException()
            }

            if (page.contains("Неверный Email или пароль")) {
                throw BadCredentialException()
            }
        }

        override fun error(e: Throwable) {
            e.printStackTrace()
        }
    }
}