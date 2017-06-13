package com.neutrino.project.accountant.client.panel.romance.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.ProfileImportHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class RomanceProfileImportHttp(val client: ReactiveClient, var page: String) : ProfileImportHttp {

    override fun import(): Mono<String?>? {
        return client
                .get("/profile/?f[status]=1$page")
                .map { ClientUtil.stringBodyAndClose(it) }
    }
}