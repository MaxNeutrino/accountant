package com.neutrino.project.accountant.parser.panel.natasha.form

import com.neutrino.project.accountant.util.DateUtil
import java.time.LocalDate


class NatashaStatisticForm(val dateRange: Pair<LocalDate, LocalDate>) {

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