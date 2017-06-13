package com.neutrino.project.accountant.client.panel.dream.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.dream.parser.DreamProfileImportHandler
import com.neutrino.project.accountant.client.service.ProfileImportService
import reactor.core.publisher.Flux


class DreamProfileImportService(val client: ReactiveClient): ProfileImportService {

    val handler = DreamProfileImportHandler(client)

    override fun import(): Flux<Profile>? {
        return handler.handle()
    }
}