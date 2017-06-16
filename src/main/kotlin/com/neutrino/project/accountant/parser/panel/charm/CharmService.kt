package com.neutrino.project.accountant.parser.panel.charm

import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.ResponseListener
import com.neutrino.project.accountant.parser.ParserService
import com.neutrino.project.accountant.parser.database.ProfileStore
import com.neutrino.project.accountant.parser.form.LoginForm
import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.util.ClientUtil
import com.neutrino.project.accountant.util.exception.BadCredentialException
import okhttp3.Response
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import java.time.LocalDate


class CharmService(private val client: ReactiveClient, dateRange: Pair<LocalDate, LocalDate>): ParserService {

    private val logger = LogManager.getLogger(this)

    private val listener = CharmResponseListener()

    private val httpController = CharmHttpController(client)
    private val profileImportHandler = CharmProfileParserHandler(httpController)
    private val statisticHandler = CharmStatisticParserHandler(httpController, dateRange)

    override fun auth(credential: LoginForm) {
        logger.info("auth")
        httpController.requestAndSubscribe(credential.get(), listener)
    }

    override fun profilesImport(): Flux<Profile> {
        logger.info("profilesImport")
        return profileImportHandler.handle(Unit)
    }

    override fun statistics(): Flux<Statistic> {
        logger.info("statistics")
        val profiles: Flux<Profile> = ProfileStore.getBySite(Site.CHARM)
        return statisticHandler.handle(profiles)
                .map {
                    val statistic = ClientUtil.converStatistic(it, client.name())
                    statistic.pay = statistic.pay * 1.5
                    statistic
                }
    }

    inner class CharmResponseListener: ResponseListener {

        override fun success(response: Response) {
            logger.info(response)
            if (!ClientUtil.stringBodyAndClose(response)?.contains("Overview")!!)
                throw BadCredentialException()
        }

        override fun error(e: Throwable) {
            logger.error("Error in request time", e)
        }
    }
}