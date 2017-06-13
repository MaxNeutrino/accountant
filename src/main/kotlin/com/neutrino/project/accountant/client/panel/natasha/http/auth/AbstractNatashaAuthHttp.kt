package com.neutrino.project.accountant.client.panel.natasha.http.auth

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.AuthHttp
import com.neutrino.project.accountant.client.listener.ResponseListener


abstract class AbstractNatashaAuthHttp(val client: ReactiveClient) : AuthHttp {

    protected fun auth(url: String, credential: Map<String, String>, listener: ResponseListener) {
        client.post(url, credential)
                .subscribe(
                        { v -> listener.success(v) },
                        { e -> listener.error(e) }
                )
    }
}