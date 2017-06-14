package com.neutrino.project.accountant.parser.panel.chatos.support.anastasia

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.panel.chatos.ChatOsHttpController
import com.neutrino.project.accountant.util.ClientUtil
import reactor.core.publisher.Mono


class ChatOsAnastasiaHttpController(private val anastasiaClient: ReactiveClient) : ChatOsHttpController(anastasiaClient) {


    override fun request(params: Map<String, String>): Mono<String> {
        return anastasiaClient.get("/customer/svadbaProfitReport${ClientUtil.mapToUrl(params)}")
                .map { ClientUtil.stringBodyAndClose(it) }
    }
}