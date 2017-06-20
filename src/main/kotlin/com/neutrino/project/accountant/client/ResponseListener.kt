package com.neutrino.project.accountant.client

import okhttp3.Response

/**
 * For subscribe from Mono or Flux
 *
 * @see Response
 */
interface ResponseListener {

    fun success(response: Response)

    fun error(e: Throwable)
}