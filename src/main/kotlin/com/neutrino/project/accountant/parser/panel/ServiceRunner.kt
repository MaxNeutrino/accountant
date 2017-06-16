package com.neutrino.project.accountant.parser.panel

import com.neutrino.project.accountant.parser.ParserService
import com.neutrino.project.accountant.parser.database.ProfileStore
import com.neutrino.project.accountant.parser.form.LoginForm
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.parser.panel.natasha.NatashaService
import com.neutrino.project.accountant.util.exception.AuthException
import reactor.core.publisher.Flux
import java.io.BufferedReader
import java.io.InputStreamReader


object ServiceRunner {

    fun runWithImport(parserService: ParserService, form: LoginForm): Flux<Statistic> {
        parserService.auth(form)
        ProfileStore.import(parserService)
        return parserService.statistics()
    }

    fun runWithoutImport(parserService: ParserService, form: LoginForm): Flux<Statistic> {
        parserService.auth(form)
        return parserService.statistics()
    }

    fun runNatashaCrap(parserService: ParserService, credentials: Pair<String, String>): Flux<Statistic> {
        val authHandler  = (parserService as NatashaService).authHandler
        try {
            authHandler.auth(credentials.first, credentials.second)
        } catch (e: Exception) {
            when (e.cause) {
                is AuthException -> {
                    println("ENTER CODE")
                    val reader = BufferedReader(InputStreamReader(System.`in`))
                    reader.use {
                        val code = reader.readLine()
                        authHandler.code(code)
                    }
                }
                else -> {
                    e.printStackTrace()
                }
            }
        }

        ProfileStore.import(parserService)
        return parserService.statistics()
    }
}