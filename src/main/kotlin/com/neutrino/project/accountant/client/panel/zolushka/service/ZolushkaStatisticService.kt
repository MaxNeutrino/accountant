package com.neutrino.project.accountant.client.panel.zolushka.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Statistic
import com.neutrino.project.accountant.client.panel.zolushka.http.ZolushkaStatisticHttp
import com.neutrino.project.accountant.client.panel.zolushka.parser.ZolushkaStatisticHandler
import com.neutrino.project.accountant.client.service.StatisticService
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Flux
import java.time.LocalDate


class ZolushkaStatisticService(val client: ReactiveClient, var dateRange: Pair<LocalDate, LocalDate>) : StatisticService {

    val http = ZolushkaStatisticHttp(client)
    val handler = ZolushkaStatisticHandler(dateRange, client, Pair(1.0, 1.0))

    override fun statistic(): Flux<Statistic> {
        return handler.handle()
                .map {
                    ClientUtil.converStatistic(it, client.name())
                }
    }
}