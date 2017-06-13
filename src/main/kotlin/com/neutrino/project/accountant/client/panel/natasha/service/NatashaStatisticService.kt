package com.neutrino.project.accountant.client.panel.natasha.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.database.ProfileStore
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.model.Statistic
import com.neutrino.project.accountant.client.panel.natasha.parser.statistic.NatashaStatisticHandler
import com.neutrino.project.accountant.client.service.StatisticService
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Flux
import java.time.LocalDate


class NatashaStatisticService(val client: ReactiveClient, var dateRange: Pair<LocalDate, LocalDate>): StatisticService {

    val handler = NatashaStatisticHandler(client, dateRange)

    override fun statistic(): Flux<Statistic> {
        val profiles = ProfileStore.getBySite(Site.NATASHA)
        return handler.handle(profiles)
                .map { ClientUtil.converStatistic(it, client.name()) }
    }
}