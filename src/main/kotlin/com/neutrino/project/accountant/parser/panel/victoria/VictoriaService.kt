package com.neutrino.project.accountant.parser.panel.victoria

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.ParserService
import com.neutrino.project.accountant.parser.form.LoginForm
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.util.ClientUtil
import com.neutrino.project.accountant.util.exception.BadCredentialException
import com.pushtorefresh.javac_warning_annotation.Warning
import okhttp3.Response
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import java.time.LocalDate
import kotlin.streams.toList


class VictoriaService(private val client: ReactiveClient, private val dateRange: Pair<LocalDate, LocalDate>) : ParserService {

    private val logger = LogManager.getLogger(this)

    private val listener = VictoriaResponseListener()
    private val httpController = VictoriaHttpController(client)

    private val profileHandler = VictoriaProfileHandler(httpController)
    private val statisticHandler = VictoriaStatisticHandler(httpController, dateRange)

    override fun auth(credential: LoginForm) {
        logger.info("auth")
        httpController.requestAndSubscribe(credential.get(), listener)
    }

    @Warning("Unstable")
    override fun profilesImport(): Flux<Profile> {
        logger.info("profilesImport")
        return profileHandler.handle(Unit)
    }

    override fun statistics(): Flux<Statistic> {
        logger.info("statistics")
        val statistics = statisticHandler.handle(Unit)
                .map { ClientUtil.converStatistic(it, client.name()) }

        return calculateForTranslator(statistics)
    }

    /**
     * Block code
     */
    private fun calculateForTranslator(statistics: Flux<Statistic>): Flux<Statistic> {
        val all = statistics.toStream().toList()

        val con = PayForTranslator(all).calcAndChange()
        return Flux.fromIterable(con)
        /*return statistics
                .buffer()
                .flatMapIterable { PayForTranslator(it).calcAndChange() }*/
    }

    private fun getTranslatorName(statistic: Statistic): String? {
        return statistic.profile.translator!!.name
    }

    inner class VictoriaResponseListener : ResponseListener {

        override fun success(response: Response) {
            logger.info(response)
            if (response.body()?.string()?.contains("true")!!)
                response.close()
            else {
                response.close()
                throw BadCredentialException()
            }
        }

        override fun error(e: Throwable) {
            logger.error("Error in request time", e)
        }
    }

    inner class PayForTranslator(private val statistics: List<Statistic>) {

        fun calcAndChange(): List<Statistic> {
            val translatorsStatistic = statistics
                    .filter { it.profile.siteId == "0" }
                    .map { statistic ->

                        val sum = getSumByTranslatorName(getTranslatorName(statistic)!!)

                        statistic.profile.siteId = "Translator ${getTranslatorName(statistic)}"
                        statistic.pay = statistic.pay - sum

                        return@map statistic
                    }

            // set site id = translator id
            // set pay for translators = allTranslatorSum - allProfileSum
            val converted = statistics
                    .map { one ->
                        if (one.profile.siteId == "0") {
                            val translator = translatorsStatistic
                                    .stream()
                                    .filter { getTranslatorName(one) == getTranslatorName(it) }
                                    .findFirst()
                                    .get()

                            return@map translator
                        } else {
                            return@map one
                        }
                    }
                    .filter { it.profile.siteId != "0" }

            return converted
        }

        private fun getSumByTranslatorName(name: String): Double {
            return statistics
                    .stream()
                    .filter {
                        (getTranslatorName(it) == name)
                                && (it.profile.siteId != "0")
                    }
                    .mapToDouble { it.pay }
                    .sum()
        }
    }
}