package com.neutrino.project.accountant.client

import okhttp3.Response
import reactor.core.publisher.Mono

/**
 * Wrapper over client
 * All methods wrap Response in Mono
 *
 * @see Response
 * @see Mono
 */
class ReactiveClient(val client: Client) {

    fun get(url: String): Mono<Response> = Mono.just(client.get(url))


    fun get(url: String, params: Map<String, String>): Mono<Response> = Mono.just(client.get(url, params))


    fun post(url: String, params: Map<String, String>): Mono<Response> = Mono.just(client.post(url, params))

    fun name() = client.name
}