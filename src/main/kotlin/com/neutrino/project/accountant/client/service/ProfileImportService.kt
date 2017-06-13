package com.neutrino.project.accountant.client.service

import com.neutrino.project.accountant.client.model.Profile
import reactor.core.publisher.Flux


interface ProfileImportService {

    fun import(): Flux<Profile>?
}