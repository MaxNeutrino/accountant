package com.neutrino.project.accountant.client.panel.romance.parse

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.romance.http.RomanceProfileImportHttp
import com.neutrino.project.accountant.util.exception.HtmlParseException
import reactor.core.publisher.Flux


class RomanceProfileImportHandler(val client: ReactiveClient) {

    val profileParser = RomanceProfileImportParser()
    val pageParser = RomanseProfilePageParser()
    val profileHttp = RomanceProfileImportHttp(client, "")

    fun handle(): Flux<Profile> {

        return profileHttp.import()?.flatMap {
            val profiles = profileParser.parse(it!!)

            try {
                val page = pageParser.parse(it)
                profileHttp.page = page.block()
                return@flatMap profiles.concatWith(handle())

            } catch (e: HtmlParseException) {
                return@flatMap profiles
            }
        }!!
    }
}