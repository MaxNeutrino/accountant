package com.neutrino.project.accountant.client.panel.romance.parse

import com.neutrino.project.accountant.client.parser.MonoHtmlParser
import com.neutrino.project.accountant.util.exception.HtmlParseException
import org.jsoup.Jsoup
import reactor.core.publisher.Mono


class RomanseProfilePageParser : MonoHtmlParser<String> {

    override fun parse(data: String): Mono<String> {
        val doc = Jsoup.parse(data)
        val pager = doc.getElementsByClass("pager").first()
        val links = pager.getElementsByTag("a")

        if (links.last().text().contains("Next")) {
            return Mono.just(
                    links
                            .last()
                            .attr("href")
                            .replace("/partner/profile/?f[status]=1", "")
            )
        } else {
            throw HtmlParseException()
        }
    }
}