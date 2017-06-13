package com.neutrino.project.accountant.client.panel.victoria.service

import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.victoria.http.VictoriaProfileImportHttp
import com.neutrino.project.accountant.client.panel.victoria.parser.VictoriaProfileImportParser
import com.neutrino.project.accountant.client.service.ProfileImportService
import reactor.core.publisher.Flux


class VictoriaProfileImportService(val http: VictoriaProfileImportHttp) : ProfileImportService {

    val parser = VictoriaProfileImportParser()

    override fun import(): Flux<Profile>? = parser
            .parse(
                    http.import()?.toFuture()?.get()!!
            )
}