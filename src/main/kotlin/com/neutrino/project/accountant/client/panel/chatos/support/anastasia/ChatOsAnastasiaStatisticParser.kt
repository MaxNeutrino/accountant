package com.neutrino.project.accountant.client.panel.chatos.support.anastasia

import com.neutrino.project.accountant.client.parser.HtmlParser
import com.neutrino.project.accountant.client.to.StatisticTo
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class ChatOsAnastasiaStatisticParser : HtmlParser<StatisticTo> {

    override fun parse(data: String): Flux<StatisticTo> {
        return Flux.fromIterable(
                Jsoup.parse(data)
                        .getElementsByTag("tbody")
                        .first()
                        .getElementsByTag("tr"))
                .map {
                    val info = it.getElementsByTag("td")

                    var id = info[1].text()
                    id = id.substring(id.indexOf(":") + 1)

                    val pay: Double = info[3]
                            .text()
                            .replace("$", "")
                            .toDouble() * 2

                    return@map StatisticTo(id, "", pay.toString())
                }
    }
}