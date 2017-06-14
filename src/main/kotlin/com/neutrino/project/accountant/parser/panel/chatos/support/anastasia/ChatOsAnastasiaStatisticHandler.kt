package com.neutrino.project.accountant.parser.panel.chatos.support.anastasia

import com.neutrino.project.accountant.parser.HtmlParser
import com.neutrino.project.accountant.parser.ParserHandler
import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.parser.to.StatisticTo
import com.neutrino.project.accountant.util.ClientUtil
import org.jsoup.Jsoup
import reactor.core.publisher.Flux
import java.time.LocalDate


class ChatOsAnastasiaStatisticHandler(private val client: ReactiveClient) : ParserHandler<Pair<LocalDate, LocalDate>, Flux<Statistic>> {

    private val parser = ChatOsAnastasiaStatisticParser()
    private val http = ChatOsAnastasiaHttpController(client)

    override fun handle(params: Pair<LocalDate, LocalDate>): Flux<Statistic> {
        val form = ChatOsAnastasiaStatisticForm(params)
        return http.request(form.get())
                .flatMap {
                    parser.parse(it)
                            .map {
                                ClientUtil.converStatistic(it, client.name())
                            }
                }
    }

    /**
     * INNER CLASSES
     */

    inner class ChatOsAnastasiaStatisticParser : HtmlParser<String, Flux<StatisticTo>> {

        override fun parse(data: String): Flux<StatisticTo> {
            return Flux.fromIterable(
                    Jsoup.parse(data)
                            .getElementsByTag("tbody")
                            .first()
                            .getElementsByTag("tr"))
                    .map {
                        val info = it.getElementsByTag("td")

                        var id = info[1].text()
                        id = id.substring(id.indexOf(":") + 1)

                        val pay: Double = info[3]
                                .text()
                                .replace("$", "")
                                .toDouble() * 2

                        return@map StatisticTo(id, "", pay.toString())
                    }
        }
    }
}