package com.neutrino.project.accountant.parser.panel.natasha

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.panel.natasha.form.NatashaStatisticForm
import com.neutrino.project.accountant.parser.to.StatisticTo
import org.jsoup.Jsoup
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


class NatashaStatisticHandler(private val http: HttpController, dateRange: Pair<LocalDate, LocalDate>): ParserHandler<Flux<Profile>, Flux<StatisticTo>> {

    private val payParser = NatashaStatisticParser()
    private val form = NatashaStatisticForm(dateRange)

    override fun handle(params: Flux<Profile>): Flux<StatisticTo> = params
            .flatMap {
                p ->
                sendRequest(p.siteId)
                        .map {
                            StatisticTo(
                                    p.siteId,
                                    "",
                                    payParser.parse(it).blockFirst())
                        }
            }

    private fun sendRequest(id: String): Mono<String> {
        val params: Map<String, String> = form.messages(id)
        return http.request(params)
    }

    /**
     * INNER CLASSES
     */

    inner class NatashaStatisticParser: HtmlParser<String, Flux<String>> {

        override fun parse(data: String): Flux<String> = Flux.just(
                Jsoup.parse(data)
                        .getElementsByClass("panel")
                        .last()
                        .getElementsByTag("td")
                        .last()
                        .text()
                        .trim()
        )
    }
}