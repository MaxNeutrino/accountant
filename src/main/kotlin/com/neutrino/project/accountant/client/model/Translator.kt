package com.neutrino.project.accountant.client.model


class Translator {

    var name: String? = null
    var surname: String? = null
    var additional: MutableList<Pair<String, String>> = mutableListOf()



    constructor() {
        additional = ArrayList()
    }

    constructor(additional: MutableList<Pair<String, String>>) {
        this.additional = additional
    }

    constructor(name: String?, surname: String?, additional: MutableList<Pair<String, String>>) {
        this.name = name
        this.surname = surname
        this.additional = additional
    }

    constructor(name: String?) {
        this.name = name
    }

    fun addInfo(key: String, value: String) = additional.add(Pair(key, value))

    fun removeInfo(key: String) = additional.removeIf { it.first == key }

    override fun toString(): String {
        return "Translator(name=$name, surname=$surname, additional=$additional)"
    }
}