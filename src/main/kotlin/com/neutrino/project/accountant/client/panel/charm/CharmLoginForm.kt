package com.neutrino.project.accountant.client.panel.charm

import com.neutrino.project.accountant.client.form.AbstractLoginForm


class CharmLoginForm(val agency: String, val staff: String, val password: String) : AbstractLoginForm() {

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