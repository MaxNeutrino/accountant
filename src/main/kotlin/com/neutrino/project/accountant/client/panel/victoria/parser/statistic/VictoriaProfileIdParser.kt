package com.neutrino.project.accountant.client.panel.victoria.parser.statistic

import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class VictoriaProfileIdParser : HtmlParser<String> {

    override fun parse(data: String): Flux<String> {
        val document = Jsoup.parse(data)
        val profiles = document.getElementById("profiles")
        val elements = profiles.getElementsByTag("option")
        //elements.removeAt(0)

        return Flux.fromIterable(elements.map { it.attr("value") })
    }
}