package com.neutrino.project.accountant.client.panel.chatos.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.ProfileImportHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class ChatOsProfileImportHttp(val client: ReactiveClient, var page: Int) : ProfileImportHttp {

    override fun import(): Mono<String?>? = client
            .get("/customer/accountList?page=$page")
            .map { ClientUtil.stringBodyAndClose(it) }

}