package com.neutrino.project.accountant.client.panel.charm.parser.statistic

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.charm.http.CharmStatisticHttp
import com.neutrino.project.accountant.client.to.StatisticTo
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


class CharmStatisticHandler(val client: ReactiveClient, val dateRange: Pair<LocalDate, LocalDate>) {

    val http = CharmStatisticHttp(client)
    val creditParser = CharmCreditsParser()
    val nextPageParser = CharmNextPageParser()
    var bodyBuilder: CharmStatisticHttp.CharmStatisticRequestBuilder? = null

    fun handle(profiles: Flux<Profile>): Flux<StatisticTo> {
        bodyBuilder = http.CharmStatisticRequestBuilder(dateRange)

        return profiles.map {
            StatisticTo(it.siteId, "", sum(calculate(it)).toString())
        }
    }

    fun firstPage(params: Map<String, String>): Mono<String> = http.statistic(params)

    fun nextPage(link: String): Mono<String>? = http.statisticPage(link)

    fun parseNext(page: String): Flux<String> = nextPageParser.parse(page)

    fun parseCredits(page: String): Flux<String> = creditParser.parse(page)

    private fun buildBody(womanId: String): Map<String, String> = bodyBuilder?.default(womanId)!!

    private fun sum(credits: Flux<String>): Double = credits
            .toStream()
            .mapToDouble { it.toDouble() }
            .sum()

    private fun calculate(p: Profile): Flux<String> = firstPage(buildBody(p.siteId))
            .flatMap {
                p ->
                parseNext(p)
                        .flatMap {
                            nextPage(it)?.flatMap { parseCredits(it) }
                        }
                        .concatWith(parseCredits(p))
            }
}