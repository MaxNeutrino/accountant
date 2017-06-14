package com.neutrino.project.accountant.parser.model


enum class Site(val baseUrl: String) {
    ANASTASIA(""),
    CHAT_OS("https://account.chatoptimizer.com"),
    DREAM("https://www.dream-marriage.com/adm"),
    ROMANCE("http://romancecompass.com/partner"),
    ZOLUSHKA("http://agency.zolushka.net"),
    CHARM("http://www.charmdate.com/clagt"),
    NATASHA("https://www.natashaclub.com/partner"),
    VICTORIA("http://agency.victoriabrides.com");

    companion object {
        fun contains(name: String): Site {
            return Site.values().filter { name.toUpperCase().contains(it.name) }.first()
        }
    }
}