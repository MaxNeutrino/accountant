package com.neutrino.project.accountant.client.panel.zolushka.parser

import com.neutrino.project.accountant.client.parser.MonoHtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Mono


class ZolushkaHiddenFormParser : MonoHtmlParser<Map<String, String>> {

    override fun parse(data: String): Mono<Map<String, String>> {
        println(data)
        val doc = Jsoup.parse(data)
        val hidden = doc.getElementsByClass("aspNetHidden")
        val params: MutableMap<String, String> = mutableMapOf()

        hidden.forEach {
            it.getElementsByTag("input")
                    .forEach{
                        params.put(it.attr("name"), it.attr("value"))
                    }
        }

        return Mono.just(params)
    }
}