package com.neutrino.project.accountant.parser.panel.charm

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import org.jsoup.Jsoup
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class CharmProfileParserHandler(private val httpController: HttpController): ParserHandler<Unit, Flux<Profile>> {

    private val groupParser: CharmGroupLinkParser = CharmGroupLinkParser()
    private val profileParser: CharmProfilePageParser = CharmProfilePageParser()

    override fun handle(params: Unit): Flux<Profile> = requestGroupPage()
            .flatMap {
                parseGroupPage(it)
                        .flatMap {
                            requestProfilePage(it)
                                    .flatMap {
                                        parseProfilePage(it)
                                    }
                        }
            }


    private fun requestGroupPage(): Mono<String> = httpController
            .request("/woman/women_group_follower.php")

    private fun requestProfilePage(link: String): Mono<String> = httpController
            .request("/woman/$link")

    private fun parseGroupPage(page: String): Flux<String> = groupParser.parse(page)

    private fun parseProfilePage(page: String): Flux<Profile> = profileParser.parse(page)

    /**
     * INER CLASSES
     */

    inner class CharmGroupLinkParser : HtmlParser<String, Flux<String>> {

        override fun parse(data: String): Flux<String> = Flux
                .fromIterable(
                        Jsoup.parse(data)
                                .getElementsByAttributeValueContaining("href", "status1=0")
                )
                .filter {
                    !it.text()
                            .trim()
                            .contains("0")
                }
                .map { it.attr("href") }
    }

    inner class CharmProfilePageParser: HtmlParser<String, Flux<Profile>> {

        override fun parse(data: String): Flux<Profile> = Flux
                .fromIterable(
                        Jsoup.parse(data)
                                .getElementsByAttributeValueContaining("href", "women_preview_profile")
                                .map { Profile(extractId(it.text()), extractName(it.text()), Site.CHARM) }
                )


        private fun extractId(s: String): String = s.substring(0, s.indexOf("-")).trim()

        private fun extractName(s: String): String = s.substring(s.indexOf("-") + 1).trim()
    }
}