package com.neutrino.project.accountant.parser

import com.neutrino.project.accountant.client.ClientFactory
import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.parser.panel.charm.CharmService
import com.neutrino.project.accountant.parser.panel.chatos.ChatOsService
import com.neutrino.project.accountant.parser.panel.dream.DreamService
import com.neutrino.project.accountant.parser.panel.natasha.NatashaService
import com.neutrino.project.accountant.parser.panel.romance.RomanceService
import com.neutrino.project.accountant.parser.panel.victoria.VictoriaService
import com.neutrino.project.accountant.parser.panel.zolushka.ZolushkaService
import com.neutrino.project.accountant.util.exception.NotFoundException
import java.time.LocalDate

/**
 * Detail in @link this#createService(String, Site)
 */
object ParserServiceFactory {

    private val store: MutableMap<String, ParserService> = mutableMapOf()

    var dateRange: Pair<LocalDate, LocalDate>? = null

    /**
     * Before call this method you need init {#dateRange: Pair<LocalDate, LocalDate>} by setter
     */
    fun createService(name: String, site: Site): ParserService {
        if (store.contains(name)) {
            return store[name]!!
        } else {
            val client = ClientFactory.get(name, site)
            val service = createServiceBySite(client, site)
            store.put(name, service)
            return service
        }
    }

    private fun createServiceBySite(client: ReactiveClient, site: Site): ParserService {
        if (dateRange != null) {
            when (site) {
                Site.CHAT_OS -> return ChatOsService(client, dateRange!!)
                Site.CHARM -> return CharmService(client, dateRange!!)
                Site.DREAM -> return DreamService(client, dateRange!!)
                Site.NATASHA -> return NatashaService(client, dateRange!!)
                Site.ROMANCE -> return RomanceService(client, dateRange!!)
                Site.VICTORIA -> return VictoriaService(client, dateRange!!)
                Site.ZOLUSHKA -> return ZolushkaService(client, dateRange!!)
                else -> throw NotFoundException("Site not exist")

            }
        } else {
            throw IllegalStateException("DateRange: Pair<LocalDate, LocalDate> not initialized")
        }
    }
}