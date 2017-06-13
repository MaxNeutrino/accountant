package com.neutrino.project.accountant.client.panel.romance

import com.neutrino.project.accountant.client.util.DateUtil
import java.time.LocalDate


class RomanceStatisticForm(dateRange: Pair<LocalDate, LocalDate>) {

    var params: MutableMap<String, String> = mutableMapOf()

    init {
        params.put("filter[service]", "")
        params.put("filter[girl_id]", "")

        params.put("filter[date_f]", dateRange.first.format(DateUtil.DAY_YEAR_BY_DOT))
        params.put("filter[date_t]", dateRange.second.format(DateUtil.DAY_YEAR_BY_DOT))

        params.put("filter[group_by_girl]", "1")
        params.put("submit", "показать")
    }
}