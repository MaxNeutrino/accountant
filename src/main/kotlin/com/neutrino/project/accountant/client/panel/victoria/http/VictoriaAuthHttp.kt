package com.neutrino.project.accountant.client.panel.victoria.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.AuthHttp
import com.neutrino.project.accountant.client.listener.ResponseListener


class VictoriaAuthHttp(val client: ReactiveClient) : AuthHttp {

    override fun auth(credential: Map<String, String>, listener: ResponseListener) {
        client.post("/operator/login", credential)
                .subscribe({ r -> listener.success(r) }, { e -> listener.error(e) })
    }
}