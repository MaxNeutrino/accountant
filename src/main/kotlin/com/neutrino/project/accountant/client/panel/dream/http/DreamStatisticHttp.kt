package com.neutrino.project.accountant.client.panel.dream.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.StatisticHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class DreamStatisticHttp(val client: ReactiveClient) : StatisticHttp {

    /**
     * Need two request to server(
     */
    override fun statistic(params: Map<String, String>): Mono<String>  {
        send(params)
        return send(params)
    }

    private fun send(params: Map<String, String>): Mono<String> = client
            .post("/bonuses.php", params)
            .map { ClientUtil.stringBodyAndClose(it) }
}