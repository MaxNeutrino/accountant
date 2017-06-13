package com.neutrino.project.accountant.client.panel.zolushka.parser

import com.neutrino.project.accountant.client.parser.HtmlParser
import com.neutrino.project.accountant.client.to.StatisticTo
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import reactor.core.publisher.Flux


class ZolushkaStatisticParser : HtmlParser<StatisticTo> {

    override fun parse(data: String): Flux<StatisticTo> {
        val doc = Jsoup.parse(data)
        val profilesInfo = doc.getElementsByTag("tr")
        profilesInfo.removeAt(0)
        profilesInfo.removeAt(0)

        return Flux.fromIterable(
                profilesInfo.map {
                    convert(it.getElementsByTag("td"))
                })
    }

    private fun convert(elements: Elements): StatisticTo {
        return StatisticTo(elements[0].text(), "","${elements[3].text()} ${elements[4].text()}")
    }
}