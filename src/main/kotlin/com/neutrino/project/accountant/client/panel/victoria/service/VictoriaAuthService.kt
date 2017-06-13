package com.neutrino.project.accountant.client.panel.victoria.service

import com.neutrino.project.accountant.client.form.LoginForm
import com.neutrino.project.accountant.client.listener.ResponseListener
import com.neutrino.project.accountant.client.panel.victoria.http.VictoriaAuthHttp
import com.neutrino.project.accountant.client.service.AuthService
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response


class VictoriaAuthService(val authHttp: VictoriaAuthHttp) : AuthService, ResponseListener {

    @Throws(BadCredentialException::class)
    override fun auth(credential: LoginForm) {
        authHttp.auth(credential.get(), this)
    }

    override fun success(response: Response) {
        if(response.body()?.string()?.contains("true")!!)
            response.close()
        else {
            response.close()
            throw BadCredentialException()
        }
    }

    override fun error(e: Throwable) {
        TODO("LOG ERROR")
    }
}