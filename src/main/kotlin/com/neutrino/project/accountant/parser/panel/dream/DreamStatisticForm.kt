package com.neutrino.project.accountant.parser.panel.dream

import com.neutrino.project.accountant.util.DateUtil
import java.time.LocalDate


class DreamStatisticForm(dateRange: Pair<LocalDate, LocalDate>) {

    private var params: MutableMap<String, String> = mutableMapOf()

    init {
        params.put("type", "")
        params.put("girl", "")
        params.put("day_from", DateUtil.day(dateRange.first))
        params.put("month_from", DateUtil.month(dateRange.first))
        params.put("year_from", DateUtil.year(dateRange.first))
        params.put("day_to", DateUtil.day(dateRange.second))
        params.put("month_to", DateUtil.month(dateRange.second))
        params.put("year_to", DateUtil.year(dateRange.second))
        params.put("bygirl", "1")
    }

    fun get(): Map<String, String> = params
}