package com.neutrino.project.accountant.client.service

import com.neutrino.project.accountant.client.form.LoginForm


interface AuthService {

    fun auth(credential: LoginForm)
}