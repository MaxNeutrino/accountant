package com.neutrino.project.accountant.client.panel.zolushka.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.StatisticHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class ZolushkaStatisticHttp(val client: ReactiveClient) : StatisticHttp {

    override fun statistic(params: Map<String, String>): Mono<String> =
            client.post("/agencies/reports/accounting_girls_activity.aspx", params)
                    .map { ClientUtil.stringBodyAndClose(it) }
}