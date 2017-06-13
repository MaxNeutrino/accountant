package com.neutrino.project.accountant.client.panel.zolushka.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.zolushka.ZolushkaFilterForm
import com.neutrino.project.accountant.client.panel.zolushka.http.ZolushkaProfileImportHttp
import com.neutrino.project.accountant.client.panel.zolushka.parser.ZolushkaFilterParser
import com.neutrino.project.accountant.client.panel.zolushka.parser.ZolushkaHiddenFormParser
import com.neutrino.project.accountant.client.panel.zolushka.parser.ZolushkaProfileParser
import com.neutrino.project.accountant.client.service.ProfileImportService
import com.neutrino.project.accountant.client.util.ClientUtil
import com.pushtorefresh.javac_warning_annotation.Warning
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Warning("Class work incorrect")
class ZolushkaProfileImportService(val client: ReactiveClient) : ProfileImportService {

    val importHttp = ZolushkaProfileImportHttp(client)
    val request = Request()

    val filterParser = ZolushkaFilterParser()
    val hiddenFormParser = ZolushkaHiddenFormParser()
    val profileParser = ZolushkaProfileParser()

    override fun import(): Flux<Profile>? {
        val formPage = importHttp.import()?.block()
        val form = filterForm(formPage!!).block()
        val pages = parsePages(form)
        profileParser.parse(pages).subscribe()


        return Flux.just()
    }

    private fun filterForm(page: String): Mono<ZolushkaFilterForm> = filterParser
            .parse(page)
            .doOnNext {
                it.addAll(hiddenFormParser
                        .parse(page)
                        .block())
            }

    private fun parsePages(form: ZolushkaFilterForm): Flux<String> {
        val pages: MutableList<String> = mutableListOf()

        (1.until(form.pageSize + 1)).forEach {
            form.nextPage()
            val page = request.send(form.params)
            pages.add(page.block()!!)
        }

        return Flux.fromIterable(pages)
    }


    inner class Request {

        fun send(params: Map<String, String>) =
                client.post("/agencies/profile/agencylist.aspx", params)
                        .map { ClientUtil.stringBodyAndClose(it) }!!

    }
}