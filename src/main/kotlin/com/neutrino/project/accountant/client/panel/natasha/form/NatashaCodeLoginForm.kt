package com.neutrino.project.accountant.client.panel.natasha.form

import com.neutrino.project.accountant.client.form.LoginForm


class NatashaCodeLoginForm(form: LoginForm, code: String) : AbstractNatashaLoginForm() {init {
    credential.putAll(form.get())
    custom("Code", code)
}

}