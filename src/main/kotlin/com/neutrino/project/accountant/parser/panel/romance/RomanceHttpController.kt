package com.neutrino.project.accountant.parser.panel.romance

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.util.ClientUtil
import reactor.core.publisher.Mono

class RomanceHttpController(private val client: ReactiveClient) : HttpController {

    /**
     * Authorization request
     */
    override fun requestAndSubscribe(credential: Map<String, String>, listener: ResponseListener) {
        client.post("/", credential)
                .subscribe(
                        { r -> listener.success(r) },
                        { e -> listener.error(e) }
                )
    }

    /**
     * Profiles page
     */
    override fun request(params: Map<String, String>): Mono<String> {
        if (params.contains("page")) {
            return client
                    .get("/profile/?f[status]=1${params["page"]}")
                    .map { ClientUtil.stringBodyAndClose(it) }
        } else {
            val url = generateUrl(params)
            return client
                    .get(url)
                    .map { ClientUtil.stringBodyAndClose(it) }
        }
    }

    override fun request(url: String): Mono<String> {
        return client
                .get(url.replace(Site.ROMANCE.baseUrl, ""))
                .map { ClientUtil.stringBodyAndClose(it) }
    }


    private fun generateUrl(params: Map<String, String>): String {
        var url = "/income/?"
        params.forEach { url = "$url${it.key}=${it.value}&" }
        url = url.substring(0, url.length - 1)

        return url
    }
}
