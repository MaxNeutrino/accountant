package com.neutrino.project.accountant.client.database

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.panel.chatos.parse.ChatOsProfileImportHandler
import com.neutrino.project.accountant.database.jdbi.impl.JdbiProfileRepositoryImpl
import com.neutrino.project.accountant.database.memory.InMemoryProfileRepositoryImpl
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


object ProfileStore {

    private val memory = InMemoryProfileRepositoryImpl

    init {
        val profiles = JdbiProfileRepositoryImpl().getAll()
        memory.saveAll(profiles)
    }

    fun get(id: String): Mono<Profile> {
        return memory.get(id)
    }

    fun getAll(): Flux<Profile> {
        return memory.getAll()
    }

    fun import(profileImport: ChatOsProfileImportHandler) {
        memory.saveAll(profileImport.handle())
    }

    fun getBySite(site: Site): Flux<Profile> {
        return getAll().filter{ it.site == site }
    }
}