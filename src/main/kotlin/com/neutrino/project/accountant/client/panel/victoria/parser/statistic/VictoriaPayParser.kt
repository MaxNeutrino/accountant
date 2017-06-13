package com.neutrino.project.accountant.client.panel.victoria.parser.statistic

import com.neutrino.project.accountant.client.parser.HtmlParser
import com.neutrino.project.accountant.util.exception.HtmlParseException
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class VictoriaPayParser : HtmlParser<String> {

    @Throws(HtmlParseException::class)
    override fun parse(data: String): Flux<String> {
        if (data.contains("No data"))
            return Flux.just("0")

        return Flux.just( Jsoup.parse(data)
                .getElementsByTag("tbody")
                .first()
                .getElementsByTag("td")
                .last()
                .text()
                .replace("$ ", "")
        ).doOnError { HtmlParseException(it) }
    }
}
