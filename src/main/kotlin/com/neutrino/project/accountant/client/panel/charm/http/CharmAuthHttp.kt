package com.neutrino.project.accountant.client.panel.charm.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.AuthHttp
import com.neutrino.project.accountant.client.listener.ResponseListener


class CharmAuthHttp(val client: ReactiveClient): AuthHttp {

    override fun auth(credential: Map<String, String>, listener: ResponseListener) {
        client.post("/login.php", credential)
                .subscribe({ r -> listener.success(r) }, { e -> listener.error(e) })
    }
}