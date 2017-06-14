package com.neutrino.project.accountant.parser.panel.charm

import com.neutrino.project.accountant.util.DateUtil
import java.time.LocalDate


class CharmStatisticForm(private val dateRange: Pair<LocalDate, LocalDate>) {

    var params: MutableMap<String, String>? = null

    fun default(womanId: String): Map<String, String> {
        params = mutableMapOf(
                "date_s_m" to DateUtil.month(dateRange.first),
                "date_s_d" to DateUtil.day(dateRange.first),
                "date_s_y" to DateUtil.year(dateRange.first),
                "date_e_m" to DateUtil.month(dateRange.second),
                "date_e_d" to DateUtil.day(dateRange.second),
                "date_e_y" to DateUtil.year(dateRange.second),
                "flag" to "",
                "womanid" to womanId,
                "manid" to "",
                "querysub" to "Search"
        )

        return params as MutableMap<String, String>
    }
}