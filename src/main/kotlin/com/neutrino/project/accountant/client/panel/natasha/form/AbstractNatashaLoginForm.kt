package com.neutrino.project.accountant.client.panel.natasha.form

import com.neutrino.project.accountant.client.form.AbstractLoginForm


abstract class AbstractNatashaLoginForm: AbstractLoginForm() {

    override fun login(login: String) {
        credential.put("ID", login)
    }

    override fun password(password: String) {
        credential.put("Password", password)
    }
}