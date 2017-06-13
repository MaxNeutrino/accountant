package com.neutrino.project.accountant.client.panel.victoria.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Statistic
import com.neutrino.project.accountant.client.panel.victoria.parser.statistic.VictoriaStatisticHandler
import com.neutrino.project.accountant.client.service.StatisticService
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Flux
import java.time.LocalDate


class VictoriaStatisticService(val client: ReactiveClient, var dateRange:Pair<LocalDate, LocalDate>): StatisticService {

    val handler = VictoriaStatisticHandler(client, dateRange)

    override fun statistic(): Flux<Statistic> {
        return handler.handle()
                .map {
                    ClientUtil.converStatistic(it, client.name())
                }
    }
}