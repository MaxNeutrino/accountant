package com.neutrino.project.accountant.parser.model


class Profile {

    var siteId:String
    var name:String? = null
    var surname:String? = null
    var nick:String? = null
    var site:Site? = null
    var translator: Translator? = null



    constructor(siteId: String, site: Site) {
        this.siteId = siteId
        this.site = site
    }

    constructor(siteId: String, name: String?, site: Site) {
        this.siteId = siteId
        this.name = name
        this.site = site
    }



    constructor(siteId: String, name: String?, surname: String?, nick: String?, site: Site) {
        this.siteId = siteId
        this.name = name
        this.surname = surname
        this.nick = nick
        this.site = site
    }

    constructor(siteId: String, name: String?, nick: String?, site: Site) {
        this.siteId = siteId
        this.name = name
        this.nick = nick
        this.site = site
    }

    constructor(siteId: String, name: String?, surname: String?) {
        this.siteId = siteId
        this.name = name
        this.surname = surname
    }

    constructor(siteId: String) {
        this.siteId = siteId
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Profile) return false

        if (siteId != other.siteId) return false
        if (name != other.name) return false
        if (surname != other.surname) return false
        if (nick != other.nick) return false
        if (site != other.site) return false

        return true
    }

    override fun hashCode(): Int {
        var result = siteId.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (surname?.hashCode() ?: 0)
        result = 31 * result + (nick?.hashCode() ?: 0)
        result = 31 * result + (site?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Profile(siteId='$siteId', name=$name, surname=$surname, nick=$nick, site=$site, translator=$translator)"
    }


}