package com.neutrino.project.accountant.client.panel.dream.parser

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class DreamProfileImportParser : HtmlParser<Profile> {

    override fun parse(data: String): Flux<Profile> = Flux
            .fromIterable(Jsoup.parse(data)
                    .getElementsByClass("onegirl"))
            .map {
                val info = it.getElementsByTag("b")
                return@map Profile(info[1].text(), info[0].text(), Site.DREAM)
            }
}