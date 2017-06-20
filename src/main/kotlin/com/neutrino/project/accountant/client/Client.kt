package com.neutrino.project.accountant.client

import okhttp3.*
import org.apache.logging.log4j.LogManager
import java.io.File
import java.net.CookieManager
import java.net.CookiePolicy

/**
 * Wrapper over okhttp
 *
 * @see OkHttpClient
 */
//TODO('Make external lib')
class Client(val baseUrl: String, val name: String) {

    //TODO("Simplify")

    private val logger = LogManager.getLogger(this)

    private var cookieManager: CookieManager? = null

    private val client: OkHttpClient = OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(getCookieManager()))
            .cache(getCache(name))
            .build()

    fun get(url: String): Response {
        //logger.info("$name - GET url = $url")
        return executeRequest(
                Builder("$baseUrl$url", HashMap())
                        .buildGet()
        )
    }

    fun get(url: String, params: Map<String, String>): Response {
        //logger.info("$name - GET url = $url")
        return executeRequest(
                Builder("$baseUrl$url", params)
                        .buildGet()
        )
    }

    fun post(url: String, params: Map<String, String>): Response {
        //logger.info("$name - POST url = $url")
        return executeRequest(
                Builder("$baseUrl$url", params)
                        .buildPost()
        )
    }

    fun post(url: String, params: Pair<String, String>): Response {
        //logger.info("$name - POST url = $url")
        return executeRequest(
                Builder("$baseUrl$url", mapOf(params.first to params.second))
                        .buildPost()
        )
    }

    fun stringBody(response: Response): String {
        return response.body()!!.string()
    }

    fun getCookieManager(): CookieManager {
        if (cookieManager == null) {
            cookieManager = CookieManager()
            cookieManager?.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }
        return cookieManager!!
    }

    fun cookieJar() = client.cookieJar()

    private fun getCache(child: String): Cache {
        val cacheDir = File(System.getProperty("java.io.tmpdir"), child)
        return Cache(cacheDir, 1024)
    }

    private fun executeRequest(request: Request): Response {
        return client.newCall(request).execute()
    }

    class Builder(var url: String, var params: Map<String, String>) {

        var requestBuilder: Request.Builder = Request.Builder()

        fun buildGet(): Request {
            if (params.isEmpty()) {
                return requestBuilder.url(url).get().build()
            } else {
                return getWithParams()
            }
        }

        fun buildPost(): Request {
            return requestBuilderPost()
        }

        private fun getWithParams(): Request {
            val urlBuilder = HttpUrl.Builder().host(url)

            params.forEach { (key, value) -> urlBuilder.addQueryParameter(key, value) }

            return requestBuilder
                    .url(urlBuilder.build())
                    .get()
                    .build()
        }

        private fun requestBuilderPost(): Request {
            return requestBuilder
                    .url(url)
                    .post(bodyBuilder(params))
                    .build()
        }

        private fun bodyBuilder(params: Map<String, String>): FormBody {
            val builder = FormBody.Builder()
            params.forEach { (key, value) -> builder.add(key, value) }
            return builder.build()
        }
    }
}