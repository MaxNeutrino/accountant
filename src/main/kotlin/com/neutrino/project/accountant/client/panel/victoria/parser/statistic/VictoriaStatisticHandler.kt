package com.neutrino.project.accountant.client.panel.victoria.parser.statistic

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.panel.victoria.http.VictoriaStatisticHttp
import com.neutrino.project.accountant.client.parser.HtmlParser
import com.neutrino.project.accountant.client.to.StatisticTo
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


class VictoriaStatisticHandler(val client: ReactiveClient, val dateRange: Pair<LocalDate, LocalDate>) {

    val http = VictoriaStatisticHttp(client)
    val profileIdParser: HtmlParser<String>
    val translatorIdParser: HtmlParser<String>
    val payParser: HtmlParser<String>

    val bodyBuilder: VictoriaStatisticHttp.StatisticByProfileRequestBuilder

    init {
        this.profileIdParser = VictoriaProfileIdParser()
        this.translatorIdParser = VictoriaTranslatorIdParser()
        this.payParser = VictoriaPayParser()
        this.bodyBuilder = http.StatisticByProfileRequestBuilder()
    }

    fun handle(): Flux<StatisticTo> = pageCall()?.flatMap {
        parseTranslators(it)
                .flatMap {
                    t ->
                    parseProfiles(t).flatMap {
                        parsePay(t, it)
                    }
                }
    }!!

    private fun pageCall(): Mono<String>? = http.statisticPage()

    private fun parseTranslators(page: String) = translatorIdParser.parse(page)


    private fun parseProfiles(translatorId: String) = http
            .statistic(
                    bodyBuilder
                            .byOperator(translatorId, dateRange)
            ).flatMap { profileIdParser.parse(it) }

    private fun parsePay(translatorId: String, profileId: String): Flux<StatisticTo> = http
            .statistic(bodyForPay(translatorId, profileId))
            .flatMap {
                payParser.parse(it)
                        .map {
                            StatisticTo(profileId, translatorId, it)
                        }
            }


    private fun bodyForPay(translatorId: String, profileId: String): Map<String, String> {
        bodyBuilder.byOperator(translatorId, dateRange)
        bodyBuilder.byProfile(profileId, dateRange)
        return bodyBuilder.params!!
    }
}