package com.neutrino.project.accountant.parser.panel.dream

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.util.ClientUtil
import reactor.core.publisher.Mono


class DreamHttpController(private val client: ReactiveClient) : HttpController {

    override fun requestAndSubscribe(credential: Map<String, String>, listener: ResponseListener) {
        client.post("/verify.php?next=", credential)
                .subscribe(
                        { r -> listener.success(r) },
                        { e -> listener.error(e) }
                )
    }

    override fun request(params: Map<String, String>): Mono<String> {
        if (params.contains("page")) {
            return client.get("/index.php?status=1${params["page"]}")
                    .map { ClientUtil.stringBodyAndClose(it) }
        } else {
            send(params)
            return send(params)
        }
    }

    override fun request(url: String): Mono<String> {
        return client
                .get(url)
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    private fun send(params: Map<String, String>): Mono<String> = client
            .post("/bonuses.php", params)
            .map { ClientUtil.stringBodyAndClose(it) }
}