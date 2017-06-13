package com.neutrino.project.accountant.client.panel.chatos.parse

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.chatos.http.ChatOsProfileImportHttp
import reactor.core.publisher.Flux



class ChatOsProfileImportHandler(val client: ReactiveClient) {

    private val profileParser = ChatOsProfileImportParser()
    private val paginationParser = ChatOsPagesProfileParser()

    private val http = ChatOsProfileImportHttp(client, 1)

    fun handle(): Flux<Profile> {
        val page = http.import()

        val pages: Flux<String> = page!!.flatMap {
            val maxPage = paginationParser.parse(it!!).block()
            return@flatMap Flux.just(it)
                    .concatWith(
                            Flux.fromIterable(cast(2,maxPage))
                    )
        }

        return pages.flatMap {
            profileParser.parse(it)
        }
    }

    private fun cast(current: Int, max: Int): MutableList<String?> {
        http.page = current

        if (current < max) {
            val list: MutableList<String?> = mutableListOf(http.import()!!.block())
            list.addAll(cast(current + 1, max))
            return list
        } else if(current == max) {
            return mutableListOf(http.import()!!.block())
        } else {
            return mutableListOf()
        }
    }
}