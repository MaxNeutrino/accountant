package com.neutrino.project.accountant.client.panel.zolushka

import com.neutrino.project.accountant.client.util.DateUtil
import org.jsoup.Jsoup
import java.time.LocalDate

class ZolushkaStatisticForm(dateRange: Pair<LocalDate, LocalDate>, response: String) {

    var params: MutableMap<String, String> = mutableMapOf()
    val hiddenParser = Hidden()

    init {
        params.put("ctl00\$PageContentContentPlaceHolder\$txtFromDate", dateRange.first.format(DateUtil.DAY_YEAR_BY_SLASH))
        params.put("ctl00\$PageContentContentPlaceHolder\$txtToDate", dateRange.second.format(DateUtil.DAY_YEAR_BY_SLASH))
        params.put("ctl00\$PageContentContentPlaceHolder\$btnSubmit", "Submit")
        hidden(hiddenParser.parse(response))
    }

    private fun hidden(data: Map<String, String>) {
        params.putAll(data)
    }

    fun email() {
        reportType("EMail")
    }

    fun chat() {
        reportType("Chat")
    }

    private fun reportType(type: String) {
        params.put("ctl00\$PageContentContentPlaceHolder\$ReportType", type)
    }

    inner class Hidden {

        fun parse(page: String): MutableMap<String, String> {
            val params: MutableMap<String, String> = mutableMapOf()

            Jsoup
                    .parse(page)
                    .getElementsByAttributeValue("type", "hidden")
                    .forEach { params.put(it.attr("name"), it.attr("value")) }

            return params
        }
    }
}
