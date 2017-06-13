package com.neutrino.project.accountant.client.panel.dream.parser

import com.neutrino.project.accountant.client.parser.HtmlParser
import com.neutrino.project.accountant.client.to.StatisticTo
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import reactor.core.publisher.Flux


class DreamStatisticParser : HtmlParser<StatisticTo> {

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
        id = id.substring(1, id.indexOf("]") + 1)

        val pay = info.last().text().trim().replace("$", "")

        return StatisticTo(id, "", pay)
    }
}