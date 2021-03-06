package com.neutrino.project.accountant.parser.panel.romance

import com.neutrino.project.accountant.parser.form.AbstractLoginForm


class RomanceLoginForm(login: String, password: String) : AbstractLoginForm() {

    init {
        login(login)
        password(password)
        custom("try_to_log_in", "Войти")
    }

    override fun login(login: String) {
        credential.put("login", login)
    }

    override fun password(password: String) {
        credential.put("pass", password)
    }
}