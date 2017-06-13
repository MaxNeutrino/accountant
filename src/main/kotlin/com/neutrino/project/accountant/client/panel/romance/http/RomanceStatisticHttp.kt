package com.neutrino.project.accountant.client.panel.romance.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.StatisticHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class RomanceStatisticHttp(val client: ReactiveClient) : StatisticHttp {

    override fun statistic(params: Map<String, String>): Mono<String> {
        val url = generateUrl(params)

        return client
                .get(url)
                .map { ClientUtil.stringBodyAndClose(it) }
    }

    private fun generateUrl(params: Map<String, String>): String {
        var url = "/income/?"
        params.forEach{ url = "$url${it.key}=${it.value}&" }
        url = url.substring(0, url.length - 1)

        return url
    }

}