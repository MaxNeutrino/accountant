package com.neutrino.project.accountant.client.http

import reactor.core.publisher.Mono


interface StatisticHttp {

    @Throws(UnsupportedOperationException::class)
    fun statisticPage(): Mono<String>? {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    fun statisticPage(url: String): Mono<String>? {
        throw UnsupportedOperationException()
    }

    fun statistic(params: Map<String, String>): Mono<String>
}