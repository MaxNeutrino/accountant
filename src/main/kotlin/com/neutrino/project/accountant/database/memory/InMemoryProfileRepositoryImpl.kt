package com.neutrino.project.accountant.database.memory

import com.neutrino.project.accountant.database.ProfileRepository
import com.neutrino.project.accountant.parser.model.Profile
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


object InMemoryProfileRepositoryImpl : ProfileRepository {

    private val store: MutableMap<String, Profile> = mutableMapOf()

    override fun getAll(): Flux<Profile> {
        return Flux.fromIterable(store.values)
    }

    override fun save(profile: Mono<Profile>) {
        profile.subscribe {
            save(it)
        }
    }

    override fun saveAll(profiles: Flux<Profile>) {
        profiles.subscribe { save(it) }
    }

    override fun get(id: String): Mono<Profile> {
        if (!store.contains(id))
            return Mono.just(Profile(id))
        return Mono.just(store[id])
    }

    override fun update(profile: Mono<Profile>) {
        profile.subscribe {
            update(it)
        }
    }

    override fun updateAll(profiles: Flux<Profile>) {
        profiles.subscribe { update(it) }
    }

    private fun save(profile: Profile) {
        if (store.contains(profile.siteId)) {

            store[profile.siteId]!!.site = profile.site

            if (profile.translator != null) {
                store[profile.siteId]?.translator = profile.translator
            }
        } else {
            store.put(profile.siteId, profile)
        }
    }

    private fun update(profile: Profile) {
        if (store.contains(profile.siteId)) {
            store.put(profile.siteId, profile)
        }
    }
}