package com.neutrino.project.accountant.client.panel.charm.parser.profile

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class CharmImportProfileHandler(val client: ReactiveClient) {

    val groupParser: CharmGroupLinkParser = CharmGroupLinkParser()
    val profileParser: CharmProfilePageParser = CharmProfilePageParser()

    fun handle(): Flux<Profile> = requestGroupPage()
            .flatMap {
                parseGroupPage(it)
                        .flatMap {
                            requestProfilePage(it)
                                    .flatMap {
                                        parseProfilePage(it)
                                    }
                        }
            }


    private fun requestGroupPage(): Mono<String> = client
            .get("/woman/women_group_follower.php")
            .map { ClientUtil.stringBodyAndClose(it) }

    private fun requestProfilePage(link: String): Mono<String> = client
            .get("/woman/$link")
            .map { ClientUtil.stringBodyAndClose(it) }

    private fun parseGroupPage(page: String): Flux<String> = groupParser.parse(page)

    private fun parseProfilePage(page: String): Flux<Profile> = profileParser.parse(page)
}