package com.neutrino.project.accountant.client.cookie

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.neutrino.project.accountant.client.Client
import okhttp3.Cookie
import okhttp3.HttpUrl
import reactor.core.publisher.Flux
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.net.HttpCookie
import kotlin.streams.toList


class CookieHandler {

    companion object {
        val SAVE_DIR = File(System.getProperty("user.home") + "/.accountant/cookies")
    }

    private val jsonType = object : TypeToken<List<HttpCookie>>() {}.type

    fun saveCookie(client: Client, url: String) {
        val cookies = extractCookie(client)
        saveCookie(cookies, client.name, url)
    }

    fun saveCookie(cookies: Flux<HttpCookie>, name: String, url: String) {
        val saveFile = File(dir().absolutePath + "/$name.json")
        FileWriter(saveFile).use {
            val gson = GsonBuilder().create()
            gson.toJson(cookies.toStream().toList(), it)
        }
    }

    fun loadCookie(client: Client) {
        val cookies: Flux<Cookie> = loadCookie(client.name)
                .map { convertCookie(it) }

        client.cookieJar()
                .saveFromResponse(
                        HttpUrl.parse(client.baseUrl),
                        cookies.toStream().toList())
    }

    fun loadCookie(name: String): Flux<HttpCookie> {
        val gson = Gson()
        val json = File("$SAVE_DIR/$name.json")

        try {
            if (json.exists()) {
                JsonReader(FileReader(json)).use {
                    return Flux.fromIterable(gson.fromJson(it, jsonType))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Flux.just()
    }

    private fun extractCookie(client: Client): Flux<HttpCookie> = Flux.fromIterable(
            client
                    .getCookieManager()
                    .cookieStore
                    .cookies)

    private fun dir(): File {
        if (!SAVE_DIR.exists()) {
            SAVE_DIR.mkdirs()
        }

        return SAVE_DIR
    }

    private fun convertCookie(httpCookie: HttpCookie) = Cookie.Builder()
            .name(httpCookie.name)
            .value(httpCookie.value)
            .domain(httpCookie.domain)
            .build()


}