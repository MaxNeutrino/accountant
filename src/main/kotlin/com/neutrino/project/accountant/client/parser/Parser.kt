package com.neutrino.project.accountant.client.parser


interface Parser<in K, out T> {
    fun parse(data: K): T
}