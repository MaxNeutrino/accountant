package com.neutrino.project.accountant.parser.panel.natasha

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.util.exception.PartnerPanelException
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import reactor.core.publisher.Flux


class NatashaProfileHandler(private val httpController: HttpController) : ParserHandler<Unit, Flux<Profile>> {

    private val profileParser = NatashaProfileImportParser()
    private val pageParser = NatashaProfileNextPageParser()

    override fun handle(params: Unit): Flux<Profile> = httpController.request()
            .flatMap {
                pageParser.parse(it!!)
                        .flatMap {
                            profileParser.parse(it)
                        }
            }

    /**
     * INNER CLASSES
     */

    inner class NatashaProfileImportParser : HtmlParser<String, Flux<Profile>> {

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

    inner class NatashaProfileNextPageParser : HtmlParser<String, Flux<String>> {

        @Throws(PartnerPanelException::class)
        override fun parse(data: String): Flux<String> {
            if (data.contains("Not so fast"))
                throw PartnerPanelException("Not so fast")


            val list: MutableList<String> = mutableListOf(data)

            var lastAddedPage = data
            while (lastAddedPage.contains("Next")) {
                val e: Elements? = Jsoup.parse(lastAddedPage)
                        .getElementsByAttributeValueContaining("href", "/partner/profiles.php?profiles=Active")

                val link = e?.get(e.size - 2)?.attr("href")?.replace("/partner", "")

                val newPage = link?.let { httpController.request(it).block() }

                list.add(newPage!!)
                lastAddedPage = newPage
            }

            return Flux.fromIterable(list)
        }
    }

}