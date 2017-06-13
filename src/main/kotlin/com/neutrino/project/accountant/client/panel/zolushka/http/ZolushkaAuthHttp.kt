package com.neutrino.project.accountant.client.panel.zolushka.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.AuthHttp
import com.neutrino.project.accountant.client.listener.ResponseListener


class ZolushkaAuthHttp(val client: ReactiveClient) : AuthHttp {

    override fun auth(credential: Map<String, String>, listener: ResponseListener) {
        client
                .post("/services/LoginService.asmx/ProcessLogin", credential)
                .subscribe(
                        { r -> listener.success(r) },
                        { e -> listener.error(e) }
                )
    }
}