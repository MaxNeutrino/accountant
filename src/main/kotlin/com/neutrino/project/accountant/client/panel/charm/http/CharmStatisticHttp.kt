package com.neutrino.project.accountant.client.panel.charm.http

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.http.StatisticHttp
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CharmStatisticHttp(val client: ReactiveClient) : StatisticHttp {

    override fun statisticPage(url: String): Mono<String>? = client
            .get(
                    url.replace(Site.CHARM.baseUrl, "")
            )
            .map { ClientUtil.stringBodyAndClose(it) }


    override fun statistic(params: Map<String, String>): Mono<String> = client
            .post("/stats/stats_detail_search_result.php", params)
            .map { ClientUtil.stringBodyAndClose(it) }


    inner class CharmStatisticRequestBuilder(val dateRange: Pair<LocalDate, LocalDate>) {

        private val DAY = DateTimeFormatter.ofPattern("dd")
        private val MONTH = DateTimeFormatter.ofPattern("MM")
        private val YEAR = DateTimeFormatter.ofPattern("yyyy")

        var params: MutableMap<String, String>? = null

        fun default(womanId: String): Map<String, String> {
            params = mutableMapOf(
                    "date_s_m" to dateRange.first.format(MONTH),
                    "date_s_d" to dateRange.first.format(DAY),
                    "date_s_y" to dateRange.first.format(YEAR),
                    "date_e_m" to dateRange.second.format(MONTH),
                    "date_e_d" to dateRange.second.format(DAY),
                    "date_e_y" to dateRange.second.format(YEAR),
                    "flag" to "",
                    "womanid" to womanId,
                    "manid" to "",
                    "querysub" to "Search"
            )

            return params as MutableMap<String, String>
        }
    }
}