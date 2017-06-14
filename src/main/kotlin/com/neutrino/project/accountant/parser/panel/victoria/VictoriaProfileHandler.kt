package com.neutrino.project.accountant.parser.panel.victoria

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.parser.model.Translator
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class VictoriaProfileHandler(private val httpController: HttpController) : ParserHandler<Unit, Flux<Profile>> {

    private val profileParser = VictoriaProfileImportParser()

    override fun handle(params: Unit): Flux<Profile> {
        return httpController.request().flatMap {
            profileParser.parse(it)
        }
    }

    /**
     * INNER CLASSES
     */

    inner class VictoriaProfileImportParser : HtmlParser<String, Flux<Profile>> {

        override fun parse(data: String): Flux<Profile> {
            val doc = Jsoup.parse(data)
            val table = doc.getElementsByTag("tbody").first()

            return Mono.just(table!!.getElementsByTag("tr"))            // Mono<Elements> == Mono<List<Element>>
                    .flatMap {
                        // Flux<Profile>
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
}