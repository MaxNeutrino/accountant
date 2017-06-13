package com.neutrino.project.accountant.client.panel.dream.service

import com.neutrino.project.accountant.client.form.LoginForm
import com.neutrino.project.accountant.client.http.AuthHttp
import com.neutrino.project.accountant.client.listener.ResponseListener
import com.neutrino.project.accountant.client.service.AuthService
import com.neutrino.project.accountant.client.util.ClientUtil
import com.neutrino.project.accountant.util.exception.AuthException
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response


class DreamAuthService(val authHttp: AuthHttp): AuthService, ResponseListener {

    override fun auth(credential: LoginForm) {
        authHttp.auth(credential.get(), this)
    }

    override fun success(response: Response) {
        val page = ClientUtil.stringBodyAndClose(response)

        if (page!!.contains("Неправильная комбинация логин")) {
            throw BadCredentialException()
        }

        if (page.contains("Введите Ваш логин и пароль")) {
            throw AuthException()
        }
    }

    override fun error(e: Throwable) {
        e.printStackTrace()
    }
}