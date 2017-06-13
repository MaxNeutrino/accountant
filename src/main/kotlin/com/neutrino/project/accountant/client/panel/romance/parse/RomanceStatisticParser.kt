package com.neutrino.project.accountant.client.panel.romance.parse

import com.neutrino.project.accountant.client.parser.HtmlParser
import com.neutrino.project.accountant.client.to.StatisticTo
import org.jsoup.Jsoup
import reactor.core.publisher.Flux

class RomanceStatisticParser : HtmlParser<StatisticTo> {

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