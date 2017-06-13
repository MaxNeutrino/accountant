package com.neutrino.project.accountant.client.panel.natasha.http.auth

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.listener.ResponseListener


class NatashaAuthHttp(client: ReactiveClient) : AbstractNatashaAuthHttp(client) {

    override fun auth(credential: Map<String, String>, listener: ResponseListener) {
        auth("//index.php", credential, listener)
    }
}