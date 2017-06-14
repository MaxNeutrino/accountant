package com.neutrino.project.accountant.database.jdbi.mapper

import com.neutrino.project.accountant.parser.model.Profile
import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper
import java.sql.ResultSet


class ProfileMapper : ResultSetMapper<Profile> {

    override fun map(index: Int, r: ResultSet?, ctx: StatementContext?): Profile {
        return Profile(
                r!!.getString("site_id"),
                r.getString("name"),
                r.getString("surname")
        )
    }
}