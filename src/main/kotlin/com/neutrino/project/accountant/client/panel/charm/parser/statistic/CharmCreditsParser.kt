package com.neutrino.project.accountant.client.panel.charm.parser.statistic

import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class CharmCreditsParser: HtmlParser<String> {

    override fun parse(data: String): Flux<String> {
        val doc = Jsoup.parse(data)
        val e = doc.getElementsByClass("emf")

        try {
            e.removeAt(0)
            e.removeAt(0)
            e.removeAt(0)
        } catch (e: Exception) {}

        return Flux.fromIterable(e)
                .filter{ e.indexOf(it) % 5 == 0  }
                .map { it.text() }
    }
}