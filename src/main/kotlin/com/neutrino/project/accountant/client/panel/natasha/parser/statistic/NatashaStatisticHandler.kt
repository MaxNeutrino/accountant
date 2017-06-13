package com.neutrino.project.accountant.client.panel.natasha.parser.statistic

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.natasha.http.NatashaStatisticHttp
import com.neutrino.project.accountant.client.to.StatisticTo
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


class NatashaStatisticHandler(client: ReactiveClient, dateRange: Pair<LocalDate, LocalDate>) {

    val http = NatashaStatisticHttp(client)
    val payParser = NatashaStatisticParser()
    val paramBuilder = http.ParamsBuilder(dateRange)

    fun handle(profiles: Flux<Profile>): Flux<StatisticTo> = profiles
            .flatMap {
                p ->
                sendRequest(p.siteId)
                        .map {
                            StatisticTo(
                                    p.siteId,
                                    payParser.parse(it).blockFirst())
                        }
            }

    private fun sendRequest(id: String): Mono<String> {
        val params: Map<String, String> = paramBuilder.messages(id)
        return http.statistic(params)
    }
}