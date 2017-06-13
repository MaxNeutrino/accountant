package com.neutrino.project.accountant.client.panel.charm.service

import com.neutrino.project.accountant.client.form.LoginForm
import com.neutrino.project.accountant.client.http.AuthHttp
import com.neutrino.project.accountant.client.listener.ResponseListener
import com.neutrino.project.accountant.client.service.AuthService
import com.neutrino.project.accountant.client.util.ClientUtil
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response


class CharmAuthService(val http: AuthHttp): AuthService, ResponseListener {

    override fun auth(credential: LoginForm) {
        http.auth(credential.get(), this)
    }

    override fun success(response: Response) {
        if (!ClientUtil.stringBodyAndClose(response)?.contains("Overview")!!)
            throw BadCredentialException()
    }

    override fun error(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}