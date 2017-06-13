package com.neutrino.project.accountant.client

import com.neutrino.project.accountant.client.model.Site


object ClientFactory {

    private var clients: MutableMap<String, ReactiveClient> = HashMap()

    fun get(name: String, site: Site): ReactiveClient {
        if(clients.contains(name))
            return clients[name]!!
        else {
            val client = ReactiveClient(Client(site.baseUrl, name))
            clients.put(name, client)
            return client
        }
    }
}