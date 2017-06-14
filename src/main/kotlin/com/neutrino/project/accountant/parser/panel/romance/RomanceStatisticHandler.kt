package com.neutrino.project.accountant.parser.panel.romance

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.to.StatisticTo
import org.jsoup.Jsoup
import reactor.core.publisher.Flux
import java.time.LocalDate


class RomanceStatisticHandler(private val httpController: HttpController) : ParserHandler<Pair<LocalDate, LocalDate>, Flux<StatisticTo>> {

    private val statisticParser = RomanceStatisticParser()

    override fun handle(params: Pair<LocalDate, LocalDate>): Flux<StatisticTo> {
        return httpController.request(RomanceStatisticForm(params).params)
                .flatMap { statisticParser.parse(it) }
    }

    /**
     * INNER CLASSES
     */

    inner class RomanceStatisticParser : HtmlParser<String, Flux<StatisticTo>> {

        override fun parse(data: String): Flux<StatisticTo> {
            val doc = Jsoup.parse(data)
            val table = doc.getElementsByClass("data-table").first()
            val rows = table.getElementsByTag("tr")
            rows.removeAt(0)
            rows.removeAt(rows.indexOf(rows.last()))

            return Flux.fromIterable(rows)
                    .map {
                        val id = it.getElementsByTag("a")
                                .first()
                                .text()
                        val sum = it.getElementsByTag("td")
                                .last()
                                .text()
                                .replace("$", "")

                        return@map StatisticTo(id, "", sum)
                    }
        }
    }
}