package com.neutrino.project.accountant.parser.panel.natasha.form


class NatashaLoginForm(login: String, password: String) : AbstractNatashaLoginForm() {

    init {
        custom("_r", "/partner/?ID=partner")
        login(login)
        password(password)
    }
}