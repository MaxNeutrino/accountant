package com.neutrino.project.accountant.parser.panel.zolushka

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import reactor.core.publisher.Flux
import kotlin.streams.toList


class ZolushkaProfileHandler(private val httpController: HttpController) : ParserHandler<Unit, Flux<Profile>> {

    private val profileParser = ZolushkaProfileParser()

    override fun handle(params: Unit): Flux<Profile> {
        return httpController.request()
                .flatMap { profileParser.parse(Flux.just(it)) }
    }

    /**
     * INNER CLASSES
     */

    inner class ZolushkaProfileParser: HtmlParser<Flux<String>, Flux<Profile>> {

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
}