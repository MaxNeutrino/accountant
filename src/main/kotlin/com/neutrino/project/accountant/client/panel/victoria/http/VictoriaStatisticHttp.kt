package com.neutrino.project.accountant.client.panel.victoria.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.StatisticHttp
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class VictoriaStatisticHttp(val client: ReactiveClient) : StatisticHttp {

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun statisticPage(): Mono<String>? = client
            .get("/statistics/index")
            .map { ClientUtil.stringBodyAndClose(it) }

    override fun statistic(params: Map<String, String>): Mono<String> = client
            .post("/statistics/index", params)
            .map { ClientUtil.stringBodyAndClose(it) }

    inner class StatisticByProfileRequestBuilder {

        var params: MutableMap<String, String>? = null

        fun default(dateRange: Pair<LocalDate, LocalDate>): Map<String, String> {
            params = mutableMapOf(
                    "operator" to "0",
                    "user" to "0",
                    "type" to "0",
                    "mirror" to "0",
                    "groupBy" to "0",
                    "from" to dateRange.first.format(formatter),
                    "to" to dateRange.second.format(formatter)
            )

            return params as Map<String, String>
        }


        fun byProfile(user: String, dateRange: Pair<LocalDate, LocalDate>): Map<String, String> {
            if (params == null)
                default(dateRange)
            params!!["user"] = user

            return params as Map<String, String>
        }

        fun byOperator(operator: String, dateRange: Pair<LocalDate, LocalDate>): Map<String, String> {
            if (params == null)
                default(dateRange)
            params!!["operator"] = operator

            return params as Map<String, String>
        }
    }
}