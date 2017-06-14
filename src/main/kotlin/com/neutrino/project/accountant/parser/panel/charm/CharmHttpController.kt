package com.neutrino.project.accountant.parser.panel.charm

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.util.ClientUtil
import reactor.core.publisher.Mono

class CharmHttpController(private val client: ReactiveClient) : HttpController {

    override fun requestAndSubscribe(credential: Map<String, String>, listener: ResponseListener) {
        client.post("/login.php", credential)
                .subscribe({ r -> listener.success(r) }, { e -> listener.error(e) })
    }

    override fun request(params: Map<String, String>): Mono<String> {
        return client
                .post("/stats/stats_detail_search_result.php", params)
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    override fun request(url: String): Mono<String> {
        return client
                .get(url.replace(Site.CHARM.baseUrl, ""))
                .map { ClientUtil.stringBodyAndClose(it) }
    }
}
