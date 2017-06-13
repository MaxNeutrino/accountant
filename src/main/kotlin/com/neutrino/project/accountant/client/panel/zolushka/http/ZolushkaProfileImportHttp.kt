package com.neutrino.project.accountant.client.panel.zolushka.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.ProfileImportHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class ZolushkaProfileImportHttp(val client: ReactiveClient) : ProfileImportHttp {

    override fun import(): Mono<String?>? = client
            .get("/agencies/profile/agencylist.aspx")
            .map { ClientUtil.stringBodyAndClose(it) }
}