package com.neutrino.project.accountant.client.panel.natasha.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.ProfileImportHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class NatashaProfileImportHttp(val client: ReactiveClient):ProfileImportHttp {

    override fun import(): Mono<String?>? {
        val response = client.get("/profiles.php?profiles=Active")
        return response.map { ClientUtil.stringBodyAndClose(it) }
    }
}