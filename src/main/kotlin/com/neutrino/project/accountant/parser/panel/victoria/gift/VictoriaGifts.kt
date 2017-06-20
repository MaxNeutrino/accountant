package com.neutrino.project.accountant.parser.panel.victoria.gift


import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.to.VictoriaDTO
import com.neutrino.project.accountant.util.ClientUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.util.*

class VictoriaGifts(private val client: ReactiveClient) {

    fun getVictoriaDTOs(fromDate: LocalDate, toDate: LocalDate): Flux<VictoriaDTO> {
        //val client = authorize()
        val dateParams = setDateParams(fromDate, toDate)
        val vicResponse = client.post("/manager/real-gifts-list", dateParams)

        val allDTOs = vicResponse.flatMap {
            val response = ClientUtil.stringBodyAndClose(it)
            val document = Jsoup.parse(response)

            val dirtyGiftRows = document.getElementsByTag("tr").toList()
            val giftRows = dirtyGiftRows.subList(3, dirtyGiftRows.size)

            Flux.fromIterable(giftRows).map {
                var dto = Mono.just<VictoriaDTO>(VictoriaDTO())
                dto = getDTOwithIdentity(dto, it)
                dto = getDTOwithGiftOrder(dto, it, client)
                dto
            }
        }

        return allDTOs.flatMap { it }
    }

    private fun setDateParams(fromDate: LocalDate, toDate: LocalDate): HashMap<String, String> {
        val map = HashMap<String, String>()
        map.put("id_female", "0")
        map.put("status", "0")
        map.put("from", fromDate.toString())
        map.put("to", toDate.toString())

        return map
    }

    private fun getDTOwithIdentity(vicDTO: Mono<VictoriaDTO>, siteElement: Element): Mono<VictoriaDTO> = vicDTO.map {
        it.operatorId = siteElement.child(0).text();
        it.operatorEmail = siteElement.child(1).text()

        // once there was an exception when the name was empty
        val fullManData = siteElement.child(2).text()
        if (fullManData.length == 9) {
            it.maleName = ""
            it.maleId = fullManData.substring(1, 8)
        } else {
            it.maleName = fullManData.substring(0, fullManData.indexOf("(") - 1)
            it.maleId = fullManData.substring(fullManData.indexOf("(") + 1, fullManData.indexOf(")"))
        }

        val fullFemaleData = siteElement.child(3).text()
        it.femaleName = fullFemaleData.substring(0, fullFemaleData.indexOf("(") - 1)
        it.femaleId = fullFemaleData.substring(fullFemaleData.indexOf("(") + 1, fullFemaleData.indexOf(")"))

        it.orderDate = siteElement.child(4).text();

        it
    }


    private fun getDTOwithGiftOrder(vicDTO: Mono<VictoriaDTO>, siteElement: Element, client: ReactiveClient): Mono<VictoriaDTO> {
        val orderDetailsPageURI = siteElement.child(5).toString().substring(
                siteElement.child(5).toString().indexOf("\"") + 1,
                siteElement.child(5).toString().lastIndexOf("\""))

        val vicOrderResponse = client.get("/manager/$orderDetailsPageURI")

        val orderDTO = vicOrderResponse.map {
            val tempOrderDTO = VictoriaDTO()

            val statusPage = ClientUtil.stringBodyAndClose(it)
            val statusDocument = Jsoup.parse(statusPage)

            val orderItems = statusDocument.getElementsByClass("col-md-5")
            val orderRows = orderItems.select("tr")
            tempOrderDTO.totalPrice = orderRows.last().child(1).text()
            orderRows.removeAt(0)
            orderRows.remove(orderRows.last())

            for (e in orderRows) {
                val giftName = e.child(1).text().trim()
                val giftAmount = e.child(2).text().trim()
                val giftAgencyPrice = e.child(3).text().trim()

                // separator is " : "
                tempOrderDTO.orderItems.add("$giftName : $giftAmount : $giftAgencyPrice")
            }

            tempOrderDTO
        }

        val res = Flux.zip(vicDTO, orderDTO).map {
            val result = it.t1
            result.totalPrice = it.t2.totalPrice
            result.orderItems = it.t2.orderItems

            result
        }.next()

        return res
    }
}