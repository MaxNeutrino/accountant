package com.neutrino.project.accountant.parser.panel.chatos

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import org.jsoup.Jsoup
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class ChatOsProfileParserHandler(private val httpController: HttpController) : ParserHandler<Unit, Flux<Profile>> {

    private val profileParser = ChatOsProfileImportParser()
    private val paginationParser = ChatOsPagesProfileParser()

    //private val http = ChatOsProfileImportHttp(client, 1)

    override fun handle(u: Unit): Flux<Profile> {
        val page = httpController.request(mapOf("page" to "1"))

        val pages: Flux<String> = page.flatMap {
            val maxPage = paginationParser.parse(it!!).block()
            return@flatMap Flux.just(it)
                    .concatWith(
                            Flux.fromIterable(cast(2, maxPage))
                    )
        }

        return pages.flatMap {
            profileParser.parse(it)
        }
    }

    private fun cast(current: Int, max: Int): MutableList<String?> {
        val page = mapOf("page" to current.toString())

        if (current < max) {
            val list: MutableList<String?> = mutableListOf(httpController.request(page).block())
            list.addAll(cast(current + 1, max))
            return list
        } else if (current == max) {
            return mutableListOf(httpController.request(page).block())
        } else {
            return mutableListOf()
        }
    }

    /**
     * INNER CLASSES
     */

    inner class ChatOsProfileImportParser : HtmlParser<String, Flux<Profile>> {

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

    inner class ChatOsPagesProfileParser : HtmlParser<String, Mono<Int>> {

        override fun parse(data: String): Mono<Int> {
            return Mono.just(Jsoup
                    .parse(data)
                    .getElementsByClass("page")
                    .last()
                    .text()
                    .toInt())
        }
    }
}