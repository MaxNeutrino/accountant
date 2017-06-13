package com.neutrino.project.accountant.client.panel.chatos

import com.neutrino.project.accountant.client.form.AbstractLoginForm


class ChatOsLoginForm(login: String, password: String): AbstractLoginForm() {

    init {
        login(login)
        password(password)
    }

    override fun login(login: String) {
        credential.put("username", login)
    }

    override fun password(password: String) {
        credential.put("password", password)
    }
}