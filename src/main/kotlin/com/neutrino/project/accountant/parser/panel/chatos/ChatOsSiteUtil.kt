package com.neutrino.project.accountant.parser.panel.chatos

import com.neutrino.project.accountant.parser.model.Site


object ChatOsSiteUtil {

    fun getSite(site: String): Site {
        when(site) {
            "Natashaclub" -> return Site.NATASHA
            "Svadba" -> return Site.ANASTASIA
            "Dream-marriage" -> return Site.DREAM
            "Zolushka" -> return Site.ZOLUSHKA
            "Charmingdate" -> return Site.CHARM
            "Romancecompass" -> return Site.ROMANCE

            else -> return Site.CHAT_OS
        }
    }
}