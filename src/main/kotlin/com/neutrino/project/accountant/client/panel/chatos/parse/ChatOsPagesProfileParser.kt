package com.neutrino.project.accountant.client.panel.chatos.parse

import com.neutrino.project.accountant.client.parser.MonoHtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Mono


class ChatOsPagesProfileParser: MonoHtmlParser<Int> {

    override fun parse(data: String): Mono<Int> {
        return Mono.just(Jsoup
                .parse(data)
                .getElementsByClass("page")
                .last()
                .text()
                .toInt())
    }
}