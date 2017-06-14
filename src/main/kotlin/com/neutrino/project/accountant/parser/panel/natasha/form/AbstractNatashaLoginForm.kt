package com.neutrino.project.accountant.parser.panel.natasha.form

import com.neutrino.project.accountant.parser.form.AbstractLoginForm


abstract class AbstractNatashaLoginForm: AbstractLoginForm() {

    override fun login(login: String) {
        credential.put("ID", login)
    }

    override fun password(password: String) {
        credential.put("Password", password)
    }
}