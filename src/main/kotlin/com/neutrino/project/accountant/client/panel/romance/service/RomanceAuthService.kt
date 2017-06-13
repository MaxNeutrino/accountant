package com.neutrino.project.accountant.client.panel.romance.service

import com.neutrino.project.accountant.client.form.LoginForm
import com.neutrino.project.accountant.client.listener.ResponseListener
import com.neutrino.project.accountant.client.panel.romance.http.RomanceAuthHttp
import com.neutrino.project.accountant.client.service.AuthService
import com.neutrino.project.accountant.client.util.ClientUtil
import com.neutrino.project.accountant.util.exception.AuthException
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response


class RomanceAuthService(val authHttp: RomanceAuthHttp): AuthService, ResponseListener {

    override fun auth(credential: LoginForm) {
        authHttp.auth(credential.get(), this)
    }

    @Throws(AuthException::class, BadCredentialException::class)
    override fun success(response: Response) {
        val page = ClientUtil.stringBodyAndClose(response)

        if (page!!.contains("Access denied! Please, log in")) {
            throw AuthException()
        }

        if (page.contains("Login or password is incorrect")) {
            throw BadCredentialException()
        }
    }

    override fun error(e: Throwable) {
        e.printStackTrace()
    }
}