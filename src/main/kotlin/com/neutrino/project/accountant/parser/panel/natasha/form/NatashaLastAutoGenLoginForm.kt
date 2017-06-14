package com.neutrino.project.accountant.parser.panel.natasha.form

import org.jsoup.Jsoup


class NatashaLastAutoGenLoginForm(val response: String): AbstractNatashaLoginForm() {

    init {
        parseAuthForm(response)
    }

    @Throws(UnsupportedOperationException::class)
    override fun custom(key: String, value: String) {
        UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun login(login: String) {
        UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun password(password: String) {
        UnsupportedOperationException()
    }

    private fun parseAuthForm(html: String) {
        credential = mutableMapOf()

        val document = Jsoup.parse(html)
        val elements = document.getElementsByAttributeValue("type", "hidden")

        elements.forEach { credential.put(it.attr("name"), it.attr("value")) }
    }
}