package com.neutrino.project.accountant.client.panel.charm.parser.profile

import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class CharmGroupLinkParser : HtmlParser<String> {

    override fun parse(data: String): Flux<String> = Flux
            .fromIterable(
                    Jsoup.parse(data)
                            .getElementsByAttributeValueContaining("href", "status1=0")
            )
            .filter {
                !it.text()
                        .trim()
                        .contains("0")
            }
            .map { it.attr("href") }
}