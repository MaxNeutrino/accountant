package com.neutrino.project.accountant.client.panel.chatos.support.anastasia

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Statistic
import com.neutrino.project.accountant.client.service.StatisticService
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Flux
import java.time.LocalDate


class ChatOsAnastasiaStatisticService(val client: ReactiveClient, var dateRange: Pair<LocalDate, LocalDate>) : StatisticService {

    val parser = ChatOsAnastasiaStatisticParser()
    val http = ChatOsAnastasiaStatisticHttp(client)
    val form = ChatOsAnastasiaStatisticForm(dateRange)

    override fun statistic(): Flux<Statistic> {
        return http.statistic(form.get())
                .flatMap {
                    parser.parse(it)
                            .map {
                                ClientUtil.converStatistic(it, client.name())
                            }
                }
    }
}