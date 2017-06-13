package com.neutrino.project.accountant.database

import com.neutrino.project.accountant.client.model.Profile
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface ProfileRepository {

    fun getAll(): Flux<Profile>

    fun save(profile: Mono<Profile>) {
        throw UnsupportedOperationException()
    }

    fun saveAll(profiles: Flux<Profile>) {
        throw UnsupportedOperationException()
    }

    fun get(id: String): Mono<Profile> {
        throw UnsupportedOperationException()
    }

    fun update(profile: Mono<Profile>) {
        throw UnsupportedOperationException()
    }

    fun updateAll(profiles: Flux<Profile>) {
        throw UnsupportedOperationException()
    }
}