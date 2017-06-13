package com.neutrino.project.accountant.client.panel.romance.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Statistic
import com.neutrino.project.accountant.client.panel.romance.RomanceStatisticForm
import com.neutrino.project.accountant.client.panel.romance.http.RomanceStatisticHttp
import com.neutrino.project.accountant.client.panel.romance.parse.RomanceStatisticParser
import com.neutrino.project.accountant.client.service.StatisticService
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Flux
import java.time.LocalDate


class RomanceStatisticService(val client: ReactiveClient, var dateRange: Pair<LocalDate, LocalDate>): StatisticService {

    val parser = RomanceStatisticParser()
    val http = RomanceStatisticHttp(client)

    override fun statistic(): Flux<Statistic> {
        return http.statistic(RomanceStatisticForm(dateRange).params)
                .flatMap {
                    parser.parse(it)
                            .map { ClientUtil.converStatistic(it, client.name()) }
                }
    }
}