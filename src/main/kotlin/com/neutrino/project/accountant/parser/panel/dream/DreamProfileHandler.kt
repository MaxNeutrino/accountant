package com.neutrino.project.accountant.parser.panel.dream

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class DreamProfileHandler(private val httpController: HttpController) : ParserHandler<Unit, Flux<Profile>> {

    private val pageParser = DreamPageProfileParser()
    private val profileParser = DreamProfileImportParser()

    override fun handle(u: Unit): Flux<Profile> {
        return httpController.request(mapOf("page" to "")).flatMap {

            val profiles = profileParser.parse(it!!)
            val pages = pageParser.parse(it)

            return@flatMap profiles.concatWith(pages.flatMap {
                httpController.request(mapOf("page" to it))
                        .flatMap {
                            profileParser.parse(it!!)
                        }
            })
        }!!
    }

    inner class DreamPageProfileParser : HtmlParser<String, Flux<String>> {

        override fun parse(data: String): Flux<String> = Flux
                .fromIterable(Jsoup.parse(data)
                        .getElementsByAttributeValueContaining("href", "page"))
                .map { it.attr("href").replace("?", "").replace("&status=1", "") }
    }

    inner class DreamProfileImportParser : HtmlParser<String, Flux<Profile>> {

        override fun parse(data: String): Flux<Profile> = Flux
                .fromIterable(Jsoup.parse(data)
                        .getElementsByClass("onegirl"))
                .map {
                    val info = it.getElementsByTag("b")
                    return@map Profile(info[1].text(), info[0].text(), Site.DREAM)
                }
    }
}