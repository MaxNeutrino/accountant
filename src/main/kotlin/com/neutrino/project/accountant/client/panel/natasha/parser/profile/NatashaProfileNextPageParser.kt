package com.neutrino.project.accountant.client.panel.natasha.parser.profile

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.parser.HtmlParser
import com.neutrino.project.accountant.client.util.ClientUtil
import com.neutrino.project.accountant.util.exception.PartnerPanelException
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class NatashaProfileNextPageParser(val client: ReactiveClient) : HtmlParser<String> {

    private val request = Request()

    @Throws(PartnerPanelException::class)
    override fun parse(data: String): Flux<String> {
        if (data.contains("Not so fast"))
            throw PartnerPanelException("Not so fast")


        val list: MutableList<String> = mutableListOf(data)

        var lastAddedPage = data
        while(lastAddedPage.contains("Next")) {
            val e: Elements? = Jsoup.parse(lastAddedPage)
                    .getElementsByAttributeValueContaining("href", "/partner/profiles.php?profiles=Active")

            val link = e?.get(e.size - 2)?.attr("href")?.replace("/partner", "")

            val newPage = link?.let { request.send(it).block() }

            list.add(newPage!!)
            lastAddedPage = newPage
        }

        return Flux.fromIterable(list)
    }

    private inner class Request {

        fun send(url: String): Mono<String> = client.get(url.replace(Site.NATASHA.baseUrl, ""))
                .map { ClientUtil.stringBodyAndClose(it) }
    }
}