package com.neutrino.project.accountant.client.panel.dream.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Statistic
import com.neutrino.project.accountant.client.panel.dream.DreamStatisticForm
import com.neutrino.project.accountant.client.panel.dream.http.DreamStatisticHttp
import com.neutrino.project.accountant.client.panel.dream.parser.DreamStatisticParser
import com.neutrino.project.accountant.client.service.StatisticService
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Flux
import java.time.LocalDate


class DreamStatisticService(val client: ReactiveClient, var dateRange: Pair<LocalDate, LocalDate>) : StatisticService {

    val parser = DreamStatisticParser()
    val http = DreamStatisticHttp(client)
    val form = DreamStatisticForm(dateRange)

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