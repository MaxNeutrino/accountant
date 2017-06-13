package com.neutrino.project.accountant.client.model


class Statistic {

    val profile: Profile
    val panelName: String
    val pay: Double

    constructor(profile: Profile, panelName: String, pay: Double) {
        this.profile = profile
        this.panelName = panelName
        this.pay = pay
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Statistic) return false

        if (profile != other.profile) return false
        if (panelName != other.panelName) return false
        if (pay != other.pay) return false

        return true
    }

    override fun hashCode(): Int {
        var result = profile.hashCode()
        result = 31 * result + panelName.hashCode()
        result = 31 * result + pay.hashCode()
        return result
    }

    override fun toString(): String {
        return "Statistic(profile=$profile, panelName='$panelName', pay=$pay)"
    }


}