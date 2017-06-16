package com.neutrino.project.accountant.parser.panel.zolushka

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.util.ClientUtil
import reactor.core.publisher.Mono


class ZolushkaHttpController(private val client: ReactiveClient) : HttpController {

    /**
     * Authorization request
     */
    override fun requestAndSubscribe(credential: Map<String, String>, listener: ResponseListener) {
        client
                .post("/services/LoginService.asmx/ProcessLogin", credential)
                .subscribe(
                        { r -> listener.success(r) },
                        { e -> listener.error(e) }
                )
    }

    /**
     * Accept terms
     */
    fun requestAndSubscribeTerms(credential: Map<String, String>, listener: ResponseListener) {
        client.post("/securelogin/terms.aspx", credential)
                .subscribe(
                        { r -> listener.success(r) },
                        { e -> listener.error(e) }
                )
    }

    /**
     * Statistic page
     */
    override fun request(params: Map<String, String>): Mono<String> {
        return client.post("/agencies/reports/accounting_girls_activity.aspx", params)
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    override fun request(url: String): Mono<String> {
        return client.get(url.replace(Site.ZOLUSHKA.baseUrl, ""))
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    /**
     * Profile request page
     */
    override fun request(): Mono<String> {
        return client
                .get("/agencies/profile/agencylist.aspx")
                .map { ClientUtil.stringBodyAndClose(it) }
    }
}