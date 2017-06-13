package com.neutrino.project.accountant.client.panel.dream.parser

import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class DreamPageProfileParser : HtmlParser<String> {

    override fun parse(data: String): Flux<String> = Flux
            .fromIterable(Jsoup.parse(data)
                    .getElementsByAttributeValueContaining("href", "page"))
            .map { it.attr("href").replace("?", "").replace("&status=1", "") }
}