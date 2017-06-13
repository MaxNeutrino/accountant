package com.neutrino.project.accountant.client.http

import com.neutrino.project.accountant.client.listener.ResponseListener


interface AuthHttp {

    fun auth(credential: Map<String, String>, listener: ResponseListener)
}