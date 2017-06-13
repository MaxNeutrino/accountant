package com.neutrino.project.accountant.client.util

import com.neutrino.project.accountant.client.database.ProfileStore
import com.neutrino.project.accountant.client.model.Statistic
import com.neutrino.project.accountant.client.model.Translator
import com.neutrino.project.accountant.client.to.StatisticTo
import okhttp3.Response


object ClientUtil {

    fun stringBodyAndClose(response: Response): String? {
        val body = response.body()?.string()
        response.close()
        return body
    }

    fun mapToUrl(params: Map<String, String>): String {
        val url = StringBuilder("?")
        params.forEach {
            url
                    .append(it.key)
                    .append("=")
                    .append(it.value)
                    .append("&")
        }

        return url.toString()
    }

    fun converStatistic(to: StatisticTo, panelName: String): Statistic {
        val profile = ProfileStore.get(to.profile!!).block()
        if (profile.translator == null)
            profile.translator = Translator(profile.translator)

        return Statistic(
                profile,
                panelName,
                to.pay!!.toDouble()
        )
    }
}