package com.neutrino.project.accountant.client.panel.dream

import com.neutrino.project.accountant.client.form.AbstractLoginForm


class DreamLoginForm(login: String, password: String): AbstractLoginForm() {

    init {
        login(login)
        password(password)
        custom("lg", "1")
    }

    override fun login(login: String) {
        credential.put("username", login)
    }

    override fun password(password: String) {
        credential.put("password", password)
    }
}