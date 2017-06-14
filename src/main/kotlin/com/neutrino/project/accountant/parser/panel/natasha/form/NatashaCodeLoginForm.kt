package com.neutrino.project.accountant.parser.panel.natasha.form

import com.neutrino.project.accountant.parser.form.LoginForm


class NatashaCodeLoginForm(form: LoginForm, code: String) : AbstractNatashaLoginForm() {init {
    credential.putAll(form.get())
    custom("Code", code)
}

}