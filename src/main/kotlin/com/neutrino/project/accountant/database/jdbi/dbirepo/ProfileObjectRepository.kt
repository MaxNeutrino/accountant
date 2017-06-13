package com.neutrino.project.accountant.database.jdbi.dbirepo

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.database.jdbi.mapper.ProfileMapper
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper

@RegisterMapper(ProfileMapper::class)
interface ProfileObjectRepository {

    @SqlQuery("SELECT * FROM profiles")
    fun getAll(): List<Profile>
}