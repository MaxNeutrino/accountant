package com.neutrino.project.accountant.client.panel.natasha.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.natasha.parser.profile.NatashaProfileImportHandler
import com.neutrino.project.accountant.client.service.ProfileImportService
import reactor.core.publisher.Flux


class NatashaProfileImportService(val client: ReactiveClient): ProfileImportService {

    val handler = NatashaProfileImportHandler(client)

    override fun import(): Flux<Profile>? {
        return handler.handle()
    }
}