package com.neutrino.project.accountant.client.panel.zolushka.parser

import com.neutrino.project.accountant.client.panel.zolushka.ZolushkaFilterForm
import com.neutrino.project.accountant.client.parser.MonoHtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Mono


class ZolushkaFilterParser : MonoHtmlParser<ZolushkaFilterForm> {

    override fun parse(data: String): Mono<ZolushkaFilterForm> {
        val doc = Jsoup.parse(data)
        val filters = doc.getElementById("Filters")

        val params = filters.getElementsByTag("input")
                .filter { it.attr("type") != "submit" }
                .map { it.attr("name") to it.attr("value") }
                .toMap()
                .toMutableMap()

        val pages = doc.getElementsByAttributeValueContaining("name", "CurrentPage")
                .first()
                .getElementsByTag("option")
                .size

        return Mono.just(ZolushkaFilterForm(params, pages))
    }
}