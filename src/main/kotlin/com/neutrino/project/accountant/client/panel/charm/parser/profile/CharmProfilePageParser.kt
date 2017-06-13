package com.neutrino.project.accountant.client.panel.charm.parser.profile

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.parser.HtmlParser
import org.jsoup.Jsoup
import reactor.core.publisher.Flux


class CharmProfilePageParser : HtmlParser<Profile> {

    override fun parse(data: String): Flux<Profile> = Flux
            .fromIterable(
                    Jsoup.parse(data)
                            .getElementsByAttributeValueContaining("href", "women_preview_profile")
                            .map { Profile(extractId(it.text()), extractName(it.text()), Site.CHARM) }
            )


    private fun extractId(s: String): String = s.substring(0, s.indexOf("-")).trim()

    private fun extractName(s: String): String = s.substring(s.indexOf("-") + 1).trim()
}