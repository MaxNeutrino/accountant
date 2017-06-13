package com.neutrino.project.accountant.client.panel.chatos.parse

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.panel.chatos.ChatOsSiteUtil
import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class ChatOsProfileImportParser : HtmlParser<Profile> {

    override fun parse(data: String): Flux<Profile> {
        val doc = Jsoup.parse(data)
        val e = doc.getElementsByTag("table").first()
        val rows = e.getElementsByTag("tr")
        rows.removeAt(0)
        rows.removeAt(0)

        return Flux.fromIterable(rows).map {
            val info = it.getElementsByTag("td")
            val site: Site = ChatOsSiteUtil.getSite(info[0].text())
            val id = info[2].text()
            val name = info[3].text()

            return@map Profile(id, name, site)
        }
    }
}