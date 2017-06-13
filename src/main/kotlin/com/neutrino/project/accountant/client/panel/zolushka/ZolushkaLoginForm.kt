package com.neutrino.project.accountant.client.panel.zolushka

import com.neutrino.project.accountant.client.form.AbstractLoginForm


class ZolushkaLoginForm(login: String, password: String) : AbstractLoginForm() {

    init {
        login(login)
        password(password)
    }

    override fun login(login: String) {
        credential.put("userName", login)
    }

    override fun password(password: String) {
        credential.put("password", password)
    }
}