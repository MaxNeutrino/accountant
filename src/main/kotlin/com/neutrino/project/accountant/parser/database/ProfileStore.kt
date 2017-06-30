package com.neutrino.project.accountant.parser.database

import com.neutrino.project.accountant.database.jdbi.impl.JdbiProfileRepositoryImpl
import com.neutrino.project.accountant.database.memory.InMemoryProfileRepositoryImpl
import com.neutrino.project.accountant.excel.TranslatorTableReader
import com.neutrino.project.accountant.parser.ParserService
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Handle in memory profile store
 */
object ProfileStore {

    val memory = InMemoryProfileRepositoryImpl

    init {
        //val profiles = JdbiProfileRepositoryImpl().getAll()
       // memory.saveAll(profiles)
    }

    fun get(id: String): Mono<Profile> {
        return memory.get(id)
    }

    fun getAll(): Flux<Profile> {
        return memory.getAll()
    }

    fun import(parserService: ParserService) {
        memory.saveAll(parserService.profilesImport())
    }

    fun getBySite(site: Site): Flux<Profile> {
        return getAll().filter { it.site == site }
    }
}