package com.neutrino.project.accountant.client.panel.natasha.parser.profile

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import reactor.core.publisher.Flux


class NatashaProfileImportParser : HtmlParser<Profile> {

    override fun parse(data: String): Flux<Profile> = Flux.fromIterable(
            Jsoup.parse(data)
                    .getElementsByClass("table")
                    .map { convert(it.getElementsByTag("td")) }
    )

    private fun convert(e: Elements) = Profile(
            e[0].text().trim(),
            e[5].text().trim(),
            e[4].text().trim(),
            Site.NATASHA)

}