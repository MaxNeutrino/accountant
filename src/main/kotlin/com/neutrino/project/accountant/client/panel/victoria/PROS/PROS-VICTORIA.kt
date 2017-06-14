package com.neutrino.project.accountant.client.panel.victoria.PROS

import com.neutrino.project.accountant.client.Client
import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.client.model.Site
import com.neutrino.project.accountant.client.panel.victoria.VictoriaLoginForm
import com.neutrino.project.accountant.client.panel.victoria.http.VictoriaAuthHttp
import com.neutrino.project.accountant.client.panel.victoria.service.VictoriaAuthService
import com.neutrino.project.accountant.client.to.VictoriaDTO
import com.neutrino.project.accountant.client.util.ClientUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import reactor.core.publisher.Flux
import java.util.*

fun main(args: Array<String>)
{
    val client = authorize()
    val dateParams = setDateParams()
    val vicResponse = client.post("/manager/real-gifts-list", dateParams)

    vicResponse.subscribe { println(ClientUtil.stringBodyAndClose(it)) }

    val z = vicResponse.flatMap {
        val response = ClientUtil.stringBodyAndClose(it)
        val document = Jsoup.parse(response)

        val dirtyGiftRows = document.getElementsByTag("tr").toList()
        val giftRows = dirtyGiftRows.subList(3, dirtyGiftRows.size)

        Flux.fromIterable(giftRows).map {
            var d = VictoriaDTO()
            d = getDTOwithIdentity(d, it)
            d = getDTOwithGiftOrder(d, it, client)
            d
        }
    }

    println("NOPE")
//    z.toStream().forEach { println(it) }
    z.subscribe { println(it) }
}




fun authorize() : ReactiveClient
{
    println("Entered authorization")
    val prop = Properties()
    prop.load(VictoriaDTO::class.java.getResourceAsStream("/credentials.properties"))

    val client = ReactiveClient(Client(Site.VICTORIA.baseUrl, "vic"))
    val authService = VictoriaAuthService(VictoriaAuthHttp(client))
    val form = VictoriaLoginForm(prop.getProperty("login"), prop.getProperty("password"))
    authService.auth(form)

    return client
}


fun  setDateParams(): HashMap<String, String>
{
    println("Entered setting params")
    val map = HashMap<String, String>()
    map.put("id_female", "0")
    map.put("status", "0")
/*    map.put("from", "2017-06-01")
    map.put("to", "2017-06-05")*/

    map.put("from", "2017-06-01")
    map.put("to", "2017-06-10")

    return map
}

fun getDTOwithIdentity(vicDTO: VictoriaDTO, it: Element) : VictoriaDTO
{
//    println("Identity")
    vicDTO.operatorId = it.child(0).text()
    vicDTO.operatorEmail = it.child(1).text()

    // once there was an exception when the name was empty
    val fullManData = it.child(2).text()
    if (fullManData.length == 9)
    {
        vicDTO.maleName = ""
        vicDTO.maleId = fullManData.substring(1, 8)
    }
    else
    {
        vicDTO.maleName = fullManData.substring(0, fullManData.indexOf("(") - 1)
        vicDTO.maleId = fullManData.substring(fullManData.indexOf("(") + 1, fullManData.indexOf(")"))
    }

    val fullFemaleData = it.child(3).text()
    vicDTO.femaleName = fullFemaleData.substring(0, fullFemaleData.indexOf("(") - 1)
    vicDTO.femaleId = fullFemaleData.substring(fullFemaleData.indexOf("(") + 1, fullFemaleData.indexOf(")"))

    vicDTO.orderDate = it.child(4).text();

    return vicDTO
}

fun getDTOwithGiftOrder(vicDTO: VictoriaDTO, it: Element, client: ReactiveClient) : VictoriaDTO
{
//    println("Order")
    val statusLink = it.child(5).toString().substring(
            it.child(5).toString().indexOf("\"") + 1,
//                                    it.child(5).toString().indexOf("m") + 1,
            it.child(5).toString().lastIndexOf("\""))

            val statusDirtyPage = client.get("/manager/$statusLink")

            statusDirtyPage.map {
                val statusPage = ClientUtil.stringBodyAndClose(it)
                val statusDocument = Jsoup.parse(statusPage)

                val orderItems = statusDocument.getElementsByClass("col-md-5")
                val orderRows = orderItems.select("tr")
                vicDTO.totalPrice = orderRows.last().child(1).text()
                orderRows.removeAt(0)
                orderRows.remove(orderRows.last())

                for (e in orderRows)
                {
                    val giftName = e.child(1).text().trim()
                    val giftAmount = e.child(2).text().trim()
                    val giftAgencyPrice = e.child(3).text().trim()

                    // separator is " : "
                    vicDTO.orderItems.add("$giftName : $giftAmount : $giftAgencyPrice")
                }
            }.subscribe()

    return vicDTO
}