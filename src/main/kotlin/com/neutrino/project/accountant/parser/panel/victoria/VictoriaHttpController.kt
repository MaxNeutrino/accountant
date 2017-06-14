package com.neutrino.project.accountant.parser.panel.victoria

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.util.ClientUtil
import reactor.core.publisher.Mono


class VictoriaHttpController(private val client: ReactiveClient) : HttpController {

    /**
     * Authorization request
     */
    override fun requestAndSubscribe(credential: Map<String, String>, listener: ResponseListener) {
        client.post("/operator/login", credential)
                .subscribe({ r -> listener.success(r) }, { e -> listener.error(e) })
    }

    override fun request(params: Map<String, String>): Mono<String> {
        if (params.isEmpty()) {
            return client
                    .get("/statistics/index")
                    .map { ClientUtil.stringBodyAndClose(it) }
        } else {
            return client
                    .post("/statistics/index", params)
                    .map { ClientUtil.stringBodyAndClose(it) }
        }
    }

    override fun request(url: String): Mono<String> {
        return client
                .get(url.replace(Site.VICTORIA.baseUrl, ""))
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    /**
     * Profile page request
     */
    override fun request(): Mono<String> {
        val response = client.post("/account/index", mapOf("operators" to "-1"))
        return response.map { ClientUtil.stringBodyAndClose(it) }
    }
}