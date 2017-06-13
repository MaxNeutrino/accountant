package com.neutrino.project.accountant.client.panel.victoria.parser

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.model.Translator
import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class VictoriaProfileImportParser : HtmlParser<Profile> {

    override fun parse(data: String): Flux<Profile> {
        val doc = Jsoup.parse(data)
        val table = doc.getElementsByTag("tbody").first()

        return Mono.just(table!!.getElementsByTag("tr"))            // Mono<Elements> == Mono<List<Element>>
                .flatMap {                                          // Flux<Profile>
                    Flux.fromIterable(
                            it.map { extractInfo(it) }
                    )
                }
    }

    private fun extractInfo(element: Element): Profile {
        val rows = element.getElementsByTag("td")

        val id = rows[0].text()
        var name = rows[1].text()
        name = name.substring(0, name.indexOf(",") + 1)

        val profile = Profile(id, name, Site.VICTORIA)
        profile.translator = extractEditLink(rows[4])

        return profile
    }

    private fun extractEditLink(element: Element?): Translator {
        val link = element?.getElementsByTag("a")?.last()?.attr("href")
        val translator: Translator = Translator()
        link?.let { translator.addInfo("victoriaLink", it) }

        return translator
    }
}