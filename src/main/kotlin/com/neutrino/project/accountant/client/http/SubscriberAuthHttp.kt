package com.neutrino.project.accountant.client.http

import com.neutrino.project.accountant.client.ClientSubscriber
import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.listener.ResponseListener


abstract class SubscriberAuthHttp(client: ReactiveClient) : AuthHttp {

    private val subscriber = ClientSubscriber(client)
    protected var url: String = ""

    override fun auth(credential: Map<String, String>, listener: ResponseListener) {
        println(url)
        subscriber.post(url, credential, listener)
    }

    protected abstract fun url()
}