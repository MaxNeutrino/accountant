package com.neutrino.project.accountant.client.panel.romance.parse

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class RomanceProfileImportParser : HtmlParser<Profile> {

    override fun parse(data: String): Flux<Profile> = Flux.fromIterable(
            Jsoup.parse(data)
                    .getElementsByClass("s"))
            .map {
                val info = it.getElementsByTag("b")
                return@map Profile(
                        info.first().text(),
                        info.last().text(),
                        Site.ROMANCE
                )
            }
}