package com.neutrino.project.accountant.parser.panel.victoria

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.ParserService
import com.neutrino.project.accountant.parser.form.LoginForm
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.util.ClientUtil
import com.neutrino.project.accountant.util.exception.BadCredentialException
import com.pushtorefresh.javac_warning_annotation.Warning
import okhttp3.Response
import reactor.core.publisher.Flux
import java.time.LocalDate


class VictoriaService(private val client: ReactiveClient, private val dateRange: Pair<LocalDate, LocalDate>): ParserService {

    private val listener = VictoriaResponseListener()
    private val httpController = VictoriaHttpController(client)

    private val profileHandler = VictoriaProfileHandler(httpController)
    private val statisticHandler = VictoriaStatisticHandler(httpController, dateRange)

    override fun auth(credential: LoginForm) {
        httpController.requestAndSubscribe(credential.get(), listener)
    }

    @Warning("Unstable")
    override fun profilesImport(): Flux<Profile> {
        return profileHandler.handle(Unit)
    }

    override fun statistics(): Flux<Statistic> {
        return statisticHandler.handle(Unit)
                .map { ClientUtil.converStatistic(it, client.name()) }
    }

    inner class VictoriaResponseListener : ResponseListener {

        override fun success(response: Response) {
            if (response.body()?.string()?.contains("true")!!)
                response.close()
            else {
                response.close()
                throw BadCredentialException()
            }
        }

        override fun error(e: Throwable) {
            e.printStackTrace()
        }
    }
}