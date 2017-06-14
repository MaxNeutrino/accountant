package com.neutrino.project.accountant.parser


interface HtmlParser<in K, out T> {
    fun parse(data: K): T
}