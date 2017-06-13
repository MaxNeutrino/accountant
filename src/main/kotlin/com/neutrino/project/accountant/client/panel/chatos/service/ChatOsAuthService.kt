package com.neutrino.project.accountant.client.panel.chatos.service

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.form.LoginForm
import com.neutrino.project.accountant.client.listener.ResponseListener
import com.neutrino.project.accountant.client.panel.chatos.http.ChatOsAuthHttp
import com.neutrino.project.accountant.client.service.AuthService
import com.neutrino.project.accountant.client.util.ClientUtil
import com.neutrino.project.accountant.util.exception.AuthException
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response


class ChatOsAuthService(val client: ReactiveClient) : AuthService, ResponseListener {

    private val authHttp = ChatOsAuthHttp(client)

    override fun auth(credential: LoginForm) {
        authHttp.auth(credential.get(), this)
    }

    override fun success(response: Response) {
        val page = ClientUtil.stringBodyAndClose(response)

        if (page!!.contains("Для входа в личный кабинет, пожалуйста авторизируйтесь")) {
            throw AuthException()
        }

        if (page.contains("Неверный Email или пароль")) {
            throw BadCredentialException()
        }
    }

    override fun error(e: Throwable) {
        e.printStackTrace()
    }
}