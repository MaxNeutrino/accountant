package com.neutrino.project.accountant.client.panel.chatos.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Profile
import com.neutrino.project.accountant.client.panel.chatos.parse.ChatOsProfileImportHandler
import com.neutrino.project.accountant.client.service.ProfileImportService
import reactor.core.publisher.Flux


class ChatOsProfileImportService(val client: ReactiveClient): ProfileImportService {

    val handler = ChatOsProfileImportHandler(client)

    override fun import(): Flux<Profile>? {
        return handler.handle()
    }
}