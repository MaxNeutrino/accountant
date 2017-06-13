package com.neutrino.project.accountant.client.panel.zolushka.parser

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.panel.zolushka.ZolushkaStatisticForm
import com.neutrino.project.accountant.client.panel.zolushka.http.ZolushkaStatisticHttp
import com.neutrino.project.accountant.client.to.StatisticTo
import com.neutrino.project.accountant.client.util.ClientUtil
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

// messageRules<StandardMail, VIPmail>
class ZolushkaStatisticHandler(val dateRange: Pair<LocalDate, LocalDate>, val client: ReactiveClient, val messageRules: Pair<Double, Double>) {

    val parser = ZolushkaStatisticParser()
    val http = ZolushkaStatisticHttp(client)

    fun handle(): Flux<StatisticTo> = send()
            .map { ZolushkaStatisticForm(dateRange, it) }
            .flatMap { get(it) }


    private fun get(form: ZolushkaStatisticForm): Flux<StatisticTo> {
        form.chat()
        val chatParams = form.params
        val chat: Flux<StatisticTo> = http.statistic(chatParams)
                .flatMap { getChat(it) }

        form.email()
        val mailParams = form.params
        val message: Flux<StatisticTo> = http.statistic(mailParams)
                .flatMap { getMessage(it) }

        return union(chat, message)
    }

    private fun union(chat: Flux<StatisticTo>, message: Flux<StatisticTo>): Flux<StatisticTo> = chat
            .collectMap { it.profile }
            .flatMap {
                union(it, message)
            }


    private fun union(map: MutableMap<String?, StatisticTo>?, flux: Flux<StatisticTo>): Flux<StatisticTo> {
        val withSum: Flux<StatisticTo> = flux.map {

            if (map?.containsKey(it.profile)!!) {
                val sum = it.pay?.toDouble()?.plus(
                        map[it.profile]?.pay?.toDouble() as Double)
                it.pay = sum.toString()
                map.remove(it.profile)
                return@map it
            } else
                return@map it
        }

        val mapFlux: Flux<StatisticTo> = Flux.fromIterable(map?.values)

        return withSum.concatWith(mapFlux)
    }

    private fun getMessage(page: String): Flux<StatisticTo> = parser.parse(page)
            .map { calcMessage(it) }


    private fun calcMessage(to: StatisticTo): StatisticTo {
        val messages = to.pay?.split(" ")

        val first = (messages?.get(0)?.toDouble() as Double) * messageRules.first
        val second = (messages[1].toDouble() as Double) * messageRules.second

        to.pay = (first + second).toString()

        return to
    }

    private fun getChat(page: String): Flux<StatisticTo> = parser
            .parse(page)
            .map { calcChat(it) }


    private fun calcChat(to: StatisticTo): StatisticTo {
        val chat = to.pay?.replace("$", "")?.split(" ")

        val first = chat?.get(0)?.toDouble() as Double
        val second = chat[1].toDouble() as Double

        to.pay = (first + second).toString()

        return to
    }

    private fun send(): Mono<String> = client
            .get("/agencies/reports/accounting_girls_activity.aspx")
            .map { ClientUtil.stringBodyAndClose(it) }
}