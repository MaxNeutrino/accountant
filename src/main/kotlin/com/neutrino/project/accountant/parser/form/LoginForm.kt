package com.neutrino.project.accountant.parser.form


interface LoginForm {

    fun login(login: String)

    fun password(password: String)

    fun custom(key: String, value: String)

    fun get(): Map<String, String>
}