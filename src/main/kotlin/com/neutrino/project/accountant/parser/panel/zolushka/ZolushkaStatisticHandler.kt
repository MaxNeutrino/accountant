package com.neutrino.project.accountant.parser.panel.zolushka

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.HttpController
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.parser.to.StatisticTo
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

// messageRules<StandardMail, VIPmail>
class ZolushkaStatisticHandler(private val httpController: HttpController, private val messageRules: Pair<Double, Double>) : ParserHandler<Pair<LocalDate, LocalDate>, Flux<StatisticTo>> {

    private val parser = ZolushkaStatisticParser()
    //val http = ZolushkaStatisticHttp(client)

    override fun handle(dateRange: Pair<LocalDate, LocalDate>): Flux<StatisticTo> = send()
            .map { ZolushkaStatisticForm(dateRange, it) }
            .flatMap { get(it) }


    private fun get(form: ZolushkaStatisticForm): Flux<StatisticTo> {
        form.chat()
        val chatParams = form.params
        val chat: Flux<StatisticTo> = httpController.request(chatParams)
                .flatMap { getChat(it) }

        form.email()
        val mailParams = form.params
        val message: Flux<StatisticTo> = httpController.request(mailParams)
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

    private fun send(): Mono<String> = httpController.request("/agencies/reports/accounting_girls_activity.aspx")

    /**
     * INNER CLASSES
     */

    inner class ZolushkaStatisticParser : HtmlParser<String, Flux<StatisticTo>> {

        override fun parse(data: String): Flux<StatisticTo> {
            val doc = Jsoup.parse(data)
            val profilesInfo = doc.getElementsByTag("tr")
            profilesInfo.removeAt(0)
            profilesInfo.removeAt(0)

            return Flux.fromIterable(
                    profilesInfo.map {
                        convert(it.getElementsByTag("td"))
                    })
        }

        private fun convert(elements: Elements): StatisticTo {
            return StatisticTo(elements[0].text(), "", "${elements[3].text()} ${elements[4].text()}")
        }
    }
}