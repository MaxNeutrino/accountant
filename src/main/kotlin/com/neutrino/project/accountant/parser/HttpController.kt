package com.neutrino.project.accountant.parser

import com.neutrino.project.accountant.client.ResponseListener
import reactor.core.publisher.Mono


interface HttpController {

    fun requestAndSubscribe(credential: Map<String, String>, listener: ResponseListener)

    @Throws(UnsupportedOperationException::class)
    fun requestAndSubscribe(listener: ResponseListener) {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    fun request(): Mono<String> {
        throw UnsupportedOperationException()
    }

    fun request(params: Map<String, String>): Mono<String>

    fun request(url: String): Mono<String>
}