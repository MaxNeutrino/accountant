package com.neutrino.project.accountant.database.jdbi

import org.skife.jdbi.v2.DBI
import java.io.IOException
import java.util.*

/**
 * Create DBI and open repositories interfaces for jdbi
 *
 * @see DBI
 */
object DbiManager {

    private var dbi: DBI

    init {
        dbi = init()
    }

    fun <T> open(clazz: Class<T>): T {
        return dbi.open(clazz)
    }

    /**
     * Load properties
     */
    private fun init(): DBI {
        try {
            Class.forName("org.postgresql.Driver")
            val props = Properties()

            try {
                DbiManager::class.java.classLoader.getResourceAsStream("db.properties").use { `in` -> props.load(`in`) }
            } catch (e: IOException) {

            }

            val url = props.getProperty("db.url")
            return DBI(url)

        } catch (e: ClassNotFoundException) {

        }
        return DBI("")
    }
}