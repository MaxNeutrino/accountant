package com.neutrino.project.accountant.client.http

import reactor.core.publisher.Mono


interface ProfileImportHttp {

    fun import(): Mono<String?>?
}