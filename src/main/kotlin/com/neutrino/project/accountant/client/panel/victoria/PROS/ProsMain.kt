package com.neutrino.project.accountant.client.panel.victoria

import com.neutrino.project.accountant.client.Client
import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.panel.victoria.http.VictoriaAuthHttp
import com.neutrino.project.accountant.client.panel.victoria.service.VictoriaAuthService
import com.neutrino.project.accountant.client.to.VictoriaDTO
import com.neutrino.project.accountant.client.util.ClientUtil
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.streams.toList

fun main(args: Array<String>)
{
    val prop = Properties()
    prop.load(VictoriaDTO::class.java.getResourceAsStream("/credentials.properties"))

    val client = ReactiveClient(Client(Site.VICTORIA.baseUrl, "vic"))
    val authService = VictoriaAuthService(VictoriaAuthHttp(client))
    val form = VictoriaLoginForm(prop.getProperty("login"), prop.getProperty("password"))
    authService.auth(form)

    val tempSite = client.get("/manager/real-gifts-list").toFuture().get()
    val response = ClientUtil.stringBodyAndClose(tempSite)
    val document = Jsoup.parse(response)

    val giftRows = document.getElementsByTag("tr")
    repeat(3, { giftRows.removeAt(0) })

    val vicDTO = VictoriaDTO()
    vicDTO.operatorId = giftRows[0].child(0).text()
    vicDTO.operatorEmail = giftRows[0].child(1).text()

    val mailTemp = giftRows[0].child(2).text()
    vicDTO.maleName = mailTemp.substring(0, mailTemp.indexOf("(") - 1)
    vicDTO.maleId = mailTemp.substring(mailTemp.indexOf("(") + 1, mailTemp.indexOf(")"))

    val femaleTemp = giftRows[0].child(3).text()
    vicDTO.femaleName = femaleTemp.substring(0, femaleTemp.indexOf("(") - 1)
    vicDTO.femaleId = femaleTemp.substring(femaleTemp.indexOf("(") + 1, femaleTemp.indexOf(")"))

    // get link to the girl
    val z = giftRows[0].child(5).toString().substring(
            giftRows[0].child(5).toString().indexOf("\"") + 1,
            giftRows[0].child(5).toString().lastIndexOf("\""))

    val temp2 = client.get("/manager/view-real-gift/20403").toFuture().get()
    val statusPage = ClientUtil.stringBodyAndClose(temp2)
    val statusDocument = Jsoup.parse(statusPage)

    val orderItems = statusDocument.getElementsByClass("col-md-5")
    val orderRows = orderItems.select("tr")
    orderRows.removeAt(0)

    vicDTO.totalPrice = orderRows.last().child(1).text()
    orderRows.remove(orderRows.last())

    for (e in orderRows)
    {
        val giftName = e.child(1).text().trim()
        val giftAmount = e.child(2).text().trim()
        val giftAgencyPrice = e.child(3).text().trim()

        // separator is " : "
        vicDTO.orderItems.add("$giftName : $giftAmount : $giftAgencyPrice")
    }

    println(vicDTO)
}
