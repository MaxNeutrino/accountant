package com.neutrino.project.accountant.parser.panel.romance

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.util.exception.HtmlParseException
import org.jsoup.Jsoup
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class RomanceProfileHandler(private val httpController: HttpController) : ParserHandler<Unit, Flux<Profile>> {

    private val profileParser = RomanceProfileImportParser()
    private val pageParser = RomanceProfilePageParser()

    private var pageNum = ""

    override fun handle(u: Unit): Flux<Profile> {

        return httpController.request(mapOf("page" to pageNum)).flatMap {
            val profiles = profileParser.parse(it!!)

            try {
                val parsedPageNum = pageParser.parse(it)
                pageNum = parsedPageNum.block()
                return@flatMap profiles.concatWith(handle(Unit))

            } catch (e: HtmlParseException) {
                return@flatMap profiles
            }
        }!!
    }

    /**
     * INNER CLASSES
     */

    inner class RomanceProfileImportParser : HtmlParser<String, Flux<Profile>> {

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

    inner class RomanceProfilePageParser: HtmlParser<String, Mono<String>> {

        override fun parse(data: String): Mono<String> {
            val doc = Jsoup.parse(data)
            val pager = doc.getElementsByClass("pager").first()
            val links = pager.getElementsByTag("a")

            if (links.last().text().contains("Next")) {
                return Mono.just(
                        links
                                .last()
                                .attr("href")
                                .replace("/partner/profile/?f[status]=1", "")
                )
            } else {
                throw HtmlParseException()
            }
        }
    }
}