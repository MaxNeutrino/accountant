package com.neutrino.project.accountant.client.panel.zolushka.parser

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.parser.Parser
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import reactor.core.publisher.Flux
import kotlin.streams.toList


class ZolushkaProfileParser : Parser<Flux<String>, Flux<Profile>> {

    override fun parse(data: Flux<String>): Flux<Profile> {
        data.toStream().toList().forEach { parse(it) }

        return Flux.just()
    }

    private fun parse(page: String) {


        val f: Flux<Profile> = Flux.fromIterable(parseRows(page))
                .map {
                    convertToProfile(
                            it.getElementsByTag("td")
                    )
                }
                /*.subscribe { println(it) }*/

        f.subscribe { println(it) }

        //println(elements)
    }

    private fun parseRows(page: String): Elements {
        val doc = Jsoup.parse(page)
        val table: Element = doc.getElementById("Results")
        val rows = table.getElementsByTag("tr")
        rows.removeAt(0)
        rows.removeAt(rows.size - 1)
        return rows
    }

    private fun convertToProfile(e: Elements): Profile {
        return Profile(
                e[0].text().trim(), // id
                e[3].text().trim(), // name
                e[1].text().trim(), // nick
                Site.ZOLUSHKA)
    }
}