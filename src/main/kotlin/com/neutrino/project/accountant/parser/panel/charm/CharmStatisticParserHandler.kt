package com.neutrino.project.accountant.parser.panel.charm

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.to.StatisticTo
import org.jsoup.Jsoup
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


class CharmStatisticParserHandler(private val httpController: HttpController, private val dateRange: Pair<LocalDate, LocalDate>) : ParserHandler<Flux<Profile>, Flux<StatisticTo>> {

    private val creditParser = CharmCreditsParser()
    private val nextPageParser = CharmNextPageParser()
    private var form: CharmStatisticForm? = null

    /**
     * Pay is credits
     */
    override fun handle(params: Flux<Profile>): Flux<StatisticTo> {
        form = CharmStatisticForm(dateRange)

        return params.map {
            StatisticTo(it.siteId, "", sum(calculate(it)).toString())
        }
    }

    fun firstPage(params: Map<String, String>): Mono<String> = httpController.request(params)

    fun nextPage(link: String): Mono<String>? = httpController.request(link)

    fun parseNext(page: String): Flux<String> = nextPageParser.parse(page)

    fun parseCredits(page: String): Flux<String> = creditParser.parse(page)

    private fun buildBody(womanId: String): Map<String, String> = form?.default(womanId)!!

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

    /**
     * INNER CLASSES
     */

    inner class CharmCreditsParser : HtmlParser<String, Flux<String>> {

        override fun parse(data: String): Flux<String> {
            val doc = Jsoup.parse(data)
            val e = doc.getElementsByClass("emf")

            try {
                e.removeAt(0)
                e.removeAt(0)
                e.removeAt(0)
            } catch (e: Exception) {
            }

            return Flux.fromIterable(e)
                    .filter { e.indexOf(it) % 5 == 0 }
                    .map { it.text() }
        }
    }

    inner class CharmNextPageParser : HtmlParser<String, Flux<String>> {

        override fun parse(data: String): Flux<String> {
            if (data.contains("Page 1 of 1"))
                return Flux.just()

            return Flux.fromIterable(
                    Jsoup.parse(data)
                            .getElementsByAttributeValueContaining("src", "next.gif")
            ).map {
                it.parent()
                        .attr("href")
            }
        }
    }
}