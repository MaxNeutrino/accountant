package com.neutrino.project.accountant.parser.panel.chatos

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.util.ClientUtil
import reactor.core.publisher.Mono


open class ChatOsHttpController(private val client: ReactiveClient) : HttpController {

    override fun requestAndSubscribe(credential: Map<String, String>, listener: ResponseListener) {
        client.post("/login", credential)
                .subscribe(
                        { r -> listener.success(r) },
                        { e -> listener.error(e) }
                )
    }

    override fun request(params: Map<String, String>): Mono<String> {
        return client
                .get("/customer/accountList?page=${params["page"]}")
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    override fun request(url: String): Mono<String> {
        return client
                .get(url)
                .map { ClientUtil.stringBodyAndClose(it) }
    }
}