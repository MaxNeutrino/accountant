package com.neutrino.project.accountant.parser.panel.dream

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.to.StatisticTo
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import reactor.core.publisher.Flux
import java.time.LocalDate


class DreamStatisticHandler(private val httpController: HttpController) : ParserHandler<Pair<LocalDate, LocalDate>, Flux<StatisticTo>> {

    private val parser = DreamStatisticParser()

    override fun handle(params: Pair<LocalDate, LocalDate>): Flux<StatisticTo> {
        val form = DreamStatisticForm(params)
        return httpController.request(form.get())
                .flatMap { parser.parse(it) }
    }

    /**
     * INNER CLASSES
     */

    inner class DreamStatisticParser : HtmlParser<String, Flux<StatisticTo>> {

        override fun parse(data: String): Flux<StatisticTo> {
            val table = Jsoup.parse(data)
                    .getElementsByTag("table").last()

            val rows = table.getElementsByTag("tr")

            rows.removeAt(0)
            rows.removeAt(rows.indexOf(rows.last()))

            return Flux.fromIterable(rows).map { convert(it) }
        }

        private fun convert(element: Element): StatisticTo {
            val info = element.getElementsByTag("td")
            var id = info.first().text()
            id = id.substring(1, id.indexOf("]"))

            val pay = info.last().text().trim().replace("$", "")

            return StatisticTo(id, "", pay)
        }
    }
}