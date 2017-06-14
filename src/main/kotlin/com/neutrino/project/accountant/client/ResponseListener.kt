package com.neutrino.project.accountant.client

import okhttp3.Response


interface ResponseListener {

    fun success(response: Response)

    fun error(e: Throwable)
}