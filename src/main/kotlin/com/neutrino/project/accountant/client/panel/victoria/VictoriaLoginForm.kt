package com.neutrino.project.accountant.client.panel.victoria

import com.neutrino.project.accountant.client.form.AbstractLoginForm


class VictoriaLoginForm: AbstractLoginForm {

    private val loginKey = "email"
    private val passwordKey = "password"

    constructor()

    constructor(login: String, password: String) {
        login(login)
        password(password)
    }

    override fun login(login: String) {
        credential.put(loginKey, login)
    }

    override fun password(password: String) {
        credential.put(passwordKey, password)
    }
}