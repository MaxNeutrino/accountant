package com.neutrino.project.accountant.client.panel.natasha.parser.statistic

import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class NatashaStatisticParser : HtmlParser<String> {

    override fun parse(data: String): Flux<String> = Flux.just(
            Jsoup.parse(data)
                    .getElementsByClass("panel")
                    .last()
                    .getElementsByTag("td")
                    .last()
                    .text()
                    .trim()
    )

}