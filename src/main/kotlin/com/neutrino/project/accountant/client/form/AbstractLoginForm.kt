package com.neutrino.project.accountant.client.form


abstract class AbstractLoginForm: LoginForm {

    var credential: MutableMap<String, String> = HashMap()

    override fun custom(key: String, value: String) {
        credential.put(key, value)
    }

    override fun get(): Map<String, String> = credential
}