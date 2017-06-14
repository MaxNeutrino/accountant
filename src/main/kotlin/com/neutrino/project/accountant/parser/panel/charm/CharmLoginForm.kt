package com.neutrino.project.accountant.parser.panel.charm

import com.neutrino.project.accountant.parser.form.AbstractLoginForm


class CharmLoginForm(agency: String, staff: String, val password: String) : AbstractLoginForm() {

    init {
        custom("agentid", agency)
        login(staff)
        password(password)
        custom("agentlogin", "Login")
    }

    override fun login(login: String) {
        credential.put("staff_id", login)
    }

    override fun password(password: String) {
        credential.put("passwd", password)
    }
}