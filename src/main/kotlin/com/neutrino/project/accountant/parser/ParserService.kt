package com.neutrino.project.accountant.parser

import com.neutrino.project.accountant.parser.form.LoginForm
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Statistic
import reactor.core.publisher.Flux

interface ParserService {

    fun auth(credential: LoginForm)

    fun profilesImport(): Flux<Profile>

    fun statistics(): Flux<Statistic>
}
