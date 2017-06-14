package com.neutrino.project.accountant.parser.to


class StatisticTo {

    var profile: String? = null
    var translator: String? = null
    var pay: String? = null

    constructor()



    constructor(profile: String?, translator: String?, pay: String?) {
        this.profile = profile
        this.translator = translator
        this.pay = pay
    }

    constructor(translator: String?, pay: String?) {
        this.profile = ""
        this.translator = translator
        this.pay = pay
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatisticTo) return false

        if (profile != other.profile) return false
        if (translator != other.translator) return false
        if (pay != other.pay) return false

        return true
    }

    override fun hashCode(): Int {
        var result = profile?.hashCode() ?: 0
        result = 31 * result + (translator?.hashCode() ?: 0)
        result = 31 * result + (pay?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "StatisticTo(profile=$profile, translator=$translator, pay=$pay)"
    }
}