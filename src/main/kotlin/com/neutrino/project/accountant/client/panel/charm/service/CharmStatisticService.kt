package com.neutrino.project.accountant.client.panel.charm.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.database.ProfileStore
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.model.Statistic
import com.neutrino.project.accountant.client.panel.charm.parser.statistic.CharmStatisticHandler
import com.neutrino.project.accountant.client.service.StatisticService
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Flux
import java.time.LocalDate


class CharmStatisticService(val client: ReactiveClient, var dateRange: Pair<LocalDate, LocalDate>) : StatisticService {

    val handler = CharmStatisticHandler(client, dateRange)

    override fun statistic(): Flux<Statistic> {
        val profiles: Flux<Profile> = ProfileStore.getBySite(Site.CHARM)
        return handler.handle(profiles)
                .map { ClientUtil.converStatistic(it, client.name()) }
    }
}