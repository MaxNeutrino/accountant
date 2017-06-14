package com.neutrino.project.accountant.parser


interface ParserHandler<in K, out T> {

    fun handle(params: K): T
}