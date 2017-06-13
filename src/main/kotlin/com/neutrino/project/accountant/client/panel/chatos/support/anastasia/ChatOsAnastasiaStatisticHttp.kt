package com.neutrino.project.accountant.client.panel.chatos.support.anastasia

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.StatisticHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono


class ChatOsAnastasiaStatisticHttp(val client: ReactiveClient): StatisticHttp {

    override fun statistic(params: Map<String, String>): Mono<String> {
        return client.get("/customer/svadbaProfitReport${ClientUtil.mapToUrl(params)}")
                .map { ClientUtil.stringBodyAndClose(it) }
    }
}