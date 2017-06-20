package com.neutrino.project.accountant.database.jdbi.dbirepo

import com.neutrino.project.accountant.database.jdbi.mapper.ProfileMapper
import com.neutrino.project.accountant.parser.model.Profile
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper

/**
 * Load all profiles from database in memory
 */
@RegisterMapper(ProfileMapper::class)
interface ProfileObjectRepository {

    @SqlQuery("SELECT * FROM profiles")
    fun getAll(): List<Profile>
}