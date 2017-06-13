package com.neutrino.project.accountant.client.panel.natasha.parser.profile

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.natasha.http.NatashaProfileImportHttp
import reactor.core.publisher.Flux


class NatashaProfileImportHandler(val client: ReactiveClient) {

    val http = NatashaProfileImportHttp(client)

    val profileParser = NatashaProfileImportParser()
    val pageParser = NatashaProfileNextPageParser(client)

    fun handle(): Flux<Profile> = http.import()!!
            .flatMap {
                pageParser.parse(it!!)
                        .flatMap {
                            profileParser.parse(it)
                        }
            }

}