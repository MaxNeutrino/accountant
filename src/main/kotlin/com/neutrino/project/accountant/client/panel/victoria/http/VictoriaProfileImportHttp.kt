package com.neutrino.project.accountant.client.panel.victoria.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.ProfileImportHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class VictoriaProfileImportHttp(val client: ReactiveClient): ProfileImportHttp {

    override fun import(): Mono<String?>? {
        val response = client.post("/account/index", mapOf("operators" to "-1"))
        return response.map { ClientUtil.stringBodyAndClose(it) }
    }
}