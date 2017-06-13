package com.neutrino.project.accountant.client.panel.dream.parser

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.dream.http.DreamProfileImportHttp
import reactor.core.publisher.Flux


class DreamProfileImportHandler(val client: ReactiveClient) {

    val pageParser = DreamPageProfileParser()
    val profileParser = DreamProfileImportParser()

    val profileHttp = DreamProfileImportHttp(client, "")

    fun handle(): Flux<Profile> {
        return profileHttp.import()?.flatMap {

            val profiles = profileParser.parse(it!!)
            val pages = pageParser.parse(it)

            return@flatMap profiles.concatWith(pages.flatMap {
                profileHttp.page = it

                profileHttp.import()!!
                        .flatMap {
                            profileParser.parse(it!!)
                        }
            })
        }!!
    }
}