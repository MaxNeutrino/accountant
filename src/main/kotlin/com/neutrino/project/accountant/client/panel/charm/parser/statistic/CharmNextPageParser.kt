package com.neutrino.project.accountant.client.panel.charm.parser.statistic

import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class CharmNextPageParser : HtmlParser<String> {

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