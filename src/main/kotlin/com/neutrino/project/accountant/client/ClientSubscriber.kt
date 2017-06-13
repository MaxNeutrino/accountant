package com.neutrino.project.accountant.client

import com.neutrino.project.accountant.client.listener.ResponseListener
import okhttp3.Response
import reactor.core.publisher.Mono


class ClientSubscriber(val reactiveClient: ReactiveClient) {

    fun get(url: String, listener: ResponseListener) = subscribe(reactiveClient.get(url), listener)


    fun get(url: String, params: Map<String, String>, listener: ResponseListener) = subscribe(reactiveClient.get(url, params), listener)


    fun post(url: String, params: Map<String, String>, listener: ResponseListener) = subscribe(reactiveClient.post(url, params), listener)

    private fun subscribe(response: Mono<Response>, listener: ResponseListener) = response.subscribe(
            { r -> listener.success(r) },
            { e -> listener.error(e) }
    )
}