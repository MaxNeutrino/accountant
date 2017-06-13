package com.neutrino.project.accountant.client.panel.natasha.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.StatisticHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import com.neutrino.project.accountant.client.util.DateUtil
import reactor.core.publisher.Mono
import java.time.LocalDate


class NatashaStatisticHttp(val client: ReactiveClient) : StatisticHttp {

    override fun statistic(params: Map<String, String>): Mono<String> = client
            .post("/finance.php", params)
            .map { ClientUtil.stringBodyAndClose(it) }


    inner class ParamsBuilder(val dateRange: Pair<LocalDate, LocalDate>) {

        private var params: MutableMap<String, String> = default()

        fun messages(womanId: String): Map<String, String> {
            params["Type"] = "1"
            params["SenderID"] = womanId
            return params
        }

        fun videos(womanId: String): Map<String, String> {
            params["Type"] = "6"
            params["SenderID"] = womanId
            return params
        }

        private fun default(): MutableMap<String, String> = mutableMapOf(
                "Password2" to "",
                "period" to "1",
                "ptype" to "range",
                "sday" to DateUtil.day(dateRange.first),
                "smonth" to DateUtil.month(dateRange.first),
                "syear" to DateUtil.year(dateRange.first),
                "eday" to DateUtil.day(dateRange.second.plusDays(1)),
                "emonth" to DateUtil.month(dateRange.second.plusDays(1)),
                "eyear" to DateUtil.year(dateRange.second.plusDays(1)),
                "Type" to "1",
                "SenderID" to "",
                "RecipientID" to "",
                "sub" to "0",
                "submit" to "Filter"

        )
    }
}