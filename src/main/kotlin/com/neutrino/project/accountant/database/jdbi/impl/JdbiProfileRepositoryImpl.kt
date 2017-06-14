package com.neutrino.project.accountant.database.jdbi.impl

import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.database.ProfileRepository
import com.neutrino.project.accountant.database.jdbi.DbiManager
import com.neutrino.project.accountant.database.jdbi.dbirepo.ProfileObjectRepository
import reactor.core.publisher.Flux


class JdbiProfileRepositoryImpl: ProfileRepository {

    val objectRepository = DbiManager.open(ProfileObjectRepository::class.java)

    override fun getAll(): Flux<Profile> {
        return Flux.fromIterable(objectRepository.getAll())
    }
}