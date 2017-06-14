package com.neutrino.project.accountant.parser.panel.victoria

import com.neutrino.project.accountant.util.DateUtil
import java.time.LocalDate


class VictoriaStatisticForm {

    var params: MutableMap<String, String>? = null

    fun default(dateRange: Pair<LocalDate, LocalDate>): Map<String, String> {
        params = mutableMapOf(
                "operator" to "0",
                "user" to "0",
                "type" to "0",
                "mirror" to "0",
                "groupBy" to "0",
                "from" to dateRange.first.format(DateUtil.YEAR_DAY_BY_DASH),
                "to" to dateRange.second.format(DateUtil.YEAR_DAY_BY_DASH)
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