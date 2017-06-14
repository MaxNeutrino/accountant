package com.neutrino.project.accountant.parser.panel.zolushka


class ZolushkaFilterForm(params: MutableMap<String, String>, val pageSize: Int) {

    var params: MutableMap<String, String> = params
        set(value) {}

    private var currentPage = 1

    init {
        params.put("ctl00\$PageContentContentPlaceHolder\$Filter_AccountStatus", "1")
        params.put("ctl00\$PageContentContentPlaceHolder\$Sort", "accountnumber {0}")
        params.put("ctl00\$PageContentContentPlaceHolder\$SortDirection", "asc")
        page(currentPage)
    }

    fun addAll(data: Map<String, String>) {
        params.putAll(data)
    }

    fun page(page: Int) {
        params.put("ctl00\$PageContentContentPlaceHolder\$CurrentPage", page.toString())
    }

    fun nextPage() {
        page(currentPage)
        params.put("__EVENTTARGET", "ctl00\$PageContentContentPlaceHolder\$NextPage")
        currentPage++
    }
}