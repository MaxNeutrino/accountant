package com.neutrino.project.accountant.client.panel.romance.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.romance.parse.RomanceProfileImportHandler
import com.neutrino.project.accountant.client.service.ProfileImportService
import reactor.core.publisher.Flux


class RomanceProfileImportService(val client: ReactiveClient): ProfileImportService {

    val handler = RomanceProfileImportHandler(client)

    override fun import(): Flux<Profile>? {
        return handler.handle()
    }
}