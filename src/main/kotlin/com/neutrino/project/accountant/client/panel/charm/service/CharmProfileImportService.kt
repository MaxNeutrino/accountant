package com.neutrino.project.accountant.client.panel.charm.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.charm.parser.profile.CharmImportProfileHandler
import com.neutrino.project.accountant.client.service.ProfileImportService
import reactor.core.publisher.Flux


class CharmProfileImportService(val client: ReactiveClient): ProfileImportService {

    val handler = CharmImportProfileHandler(client)

    override fun import(): Flux<Profile>? {
        return handler.handle()
    }
}