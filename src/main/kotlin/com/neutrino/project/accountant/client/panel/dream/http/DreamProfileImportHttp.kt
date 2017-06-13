package com.neutrino.project.accountant.client.panel.dream.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.ProfileImportHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class DreamProfileImportHttp(val client: ReactiveClient, var page: String): ProfileImportHttp {

    override fun import(): Mono<String?>? {
        return client.get("/index.php?status=1$page")
                .map { ClientUtil.stringBodyAndClose(it) }
    }
}