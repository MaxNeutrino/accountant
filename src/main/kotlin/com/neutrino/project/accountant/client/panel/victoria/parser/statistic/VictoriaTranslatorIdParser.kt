package com.neutrino.project.accountant.client.panel.victoria.parser.statistic

import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import reactor.core.publisher.Flux


class VictoriaTranslatorIdParser : HtmlParser<String> {

    override fun parse(data: String): Flux<String> {
        val document: Document = Jsoup.parse(data)
        val table = document.getElementById("operators")
        val translators = table.getElementsByTag("option")
        translators.removeAt(0)
        translators.removeAt(1)

        return Flux.fromIterable(translators.map { it.attr("value") })
    }
}