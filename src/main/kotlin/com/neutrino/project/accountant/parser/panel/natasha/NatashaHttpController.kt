package com.neutrino.project.accountant.parser.panel.natasha

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.util.ClientUtil
import reactor.core.publisher.Mono


open class NatashaHttpController(val client: ReactiveClient) : HttpController {

    override fun requestAndSubscribe(credential: Map<String, String>, listener: ResponseListener) {
        auth("//index.php", credential, listener)
    }

    /**
     * Finance page
     */
    override fun request(params: Map<String, String>): Mono<String> {
        return client
                .post("/finance.php", params)
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    override fun request(url: String): Mono<String> {
        return client
                .get(url.replace(Site.NATASHA.baseUrl, ""))
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    /**
     * Profile import
     */
    override fun request(): Mono<String> {
        return client.get("/profiles.php?profiles=Active")
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    protected fun auth(url: String, credential: Map<String, String>, listener: ResponseListener) {
        client.post(url, credential)
                .subscribe(
                        { v -> listener.success(v) },
                        { e -> listener.error(e) }
                )
    }
}