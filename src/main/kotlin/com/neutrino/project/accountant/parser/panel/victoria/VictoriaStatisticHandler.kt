package com.neutrino.project.accountant.parser.panel.victoria

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.to.StatisticTo
import com.neutrino.project.accountant.util.exception.HtmlParseException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


class VictoriaStatisticHandler(private val httpController: HttpController, private val dateRange: Pair<LocalDate, LocalDate>): ParserHandler<Unit, Flux<StatisticTo>> {

    private val profileIdParser = VictoriaProfileIdParser()
    private val translatorIdParser = VictoriaTranslatorIdParser()
    private val payParser = VictoriaPayParser()

    private val form = VictoriaStatisticForm()

    override fun handle(u: Unit): Flux<StatisticTo> = pageCall()?.flatMap {
        parseTranslators(it)
                .flatMap {
                    t ->
                    parseProfiles(t).flatMap {
                        parsePay(t, it)
                    }
                }
    }!!

    private fun pageCall(): Mono<String>? = httpController.request(mapOf())

    private fun parseTranslators(page: String) = translatorIdParser.parse(page)


    private fun parseProfiles(translatorId: String) = httpController
            .request(
                    form
                            .byOperator(translatorId, dateRange)
            ).flatMap { profileIdParser.parse(it) }

    private fun parsePay(translatorId: String, profileId: String): Flux<StatisticTo> = httpController
            .request(bodyForPay(translatorId, profileId))
            .flatMap {
                payParser.parse(it)
                        .map {
                            StatisticTo(profileId, translatorId, it)
                        }
            }


    private fun bodyForPay(translatorId: String, profileId: String): Map<String, String> {
        form.byOperator(translatorId, dateRange)
        form.byProfile(profileId, dateRange)
        return form.params!!
    }

    /**
     * INNER CLASSES
     */

    inner class VictoriaPayParser: HtmlParser<String, Flux<String>> {

        @Throws(HtmlParseException::class)
        override fun parse(data: String): Flux<String> {
            if (data.contains("No data"))
                return Flux.just("0")

            return Flux.just( Jsoup.parse(data)
                    .getElementsByTag("tbody")
                    .first()
                    .getElementsByTag("td")
                    .last()
                    .text()
                    .replace("$ ", "")
            ).doOnError { HtmlParseException(it) }
        }
    }

    inner class VictoriaProfileIdParser: HtmlParser<String, Flux<String>> {

        override fun parse(data: String): Flux<String> {
            val document = Jsoup.parse(data)
            val profiles = document.getElementById("profiles")
            val elements = profiles.getElementsByTag("option")
            //elements.removeAt(0)

            return Flux.fromIterable(elements.map { it.attr("value") })
        }
    }

    inner class VictoriaTranslatorIdParser: HtmlParser<String, Flux<String>> {

        override fun parse(data: String): Flux<String> {
            val document: Document = Jsoup.parse(data)
            val table = document.getElementById("operators")
            val translators = table.getElementsByTag("option")
            translators.removeAt(0)
            translators.removeAt(1)

            return Flux.fromIterable(translators.map { it.attr("value") })
        }
    }
}