package com.neutrino.project.accountant.parser.panel.chatos.support.anastasia

import com.neutrino.project.accountant.util.DateUtil
import java.time.LocalDate


class ChatOsAnastasiaStatisticForm(dateRange: Pair<LocalDate, LocalDate>) {
    private var params: MutableMap<String, String> = mutableMapOf()

    init {
        params.put("startDate", dateRange.first.format(DateUtil.YEAR_DAY_BY_DASH))
        params.put("endDate", dateRange.second.format(DateUtil.YEAR_DAY_BY_DASH))
    }

    fun get(): Map<String, String> = params
}