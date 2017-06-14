package com.neutrino.project.accountant.parser.panel.victoria

import com.neutrino.project.accountant.client.Client
import com.neutrino.project.accountant.client.ReactiveClient
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.parser.to.VictoriaDTO
import com.neutrino.project.accountant.util.ClientUtil
import org.jsoup.Jsoup
import reactor.core.publisher.Flux
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun main(args: Array<String>)
{
    val prop = Properties()
    prop.load(VictoriaDTO::class.java.getResourceAsStream("/credentials.properties"))

    val client = ReactiveClient(Client(Site.VICTORIA.baseUrl, "vic"))
    val authService = VictoriaAuthService(VictoriaAuthHttp(client))
    val form = VictoriaLoginForm(prop.getProperty("login"), prop.getProperty("password"))
    authService.auth(form)

    val map = HashMap<String, String>()
    map.put("id_female", "0")
    map.put("status", "0")
/*    map.put("from", "2017-06-01")
    map.put("to", "2017-06-05")*/

/*    val tempSite = client.post("/manager/real-gifts-list", map).toFuture().get()
    val response = ClientUtil.stringBodyAndClose(tempSite)
    val document = Jsoup.parse(response)*/



    /*    val x = BufferedReader(InputStreamReader(VictoriaLoginForm::class.java.getResourceAsStream("/VictoriaBrides.html")))
    val page: String = x.readText()
    val document = Jsoup.parse(page)*/


    // giftElements = document.getElementsByTag("tr")






















    val tempSite = client.post("/manager/real-gifts-list", map)
    val response = ClientUtil.stringBodyAndClose(tempSite.toFuture().get())
    val document = Jsoup.parse(response)

    val giftRows =Flux.fromIterable(document.getElementsByTag("tr").toList())
    val res = giftRows.skip(3)
            .map {
                val vicDTO = VictoriaDTO()
                vicDTO.operatorId = it.child(0).text()
                vicDTO.operatorEmail = it.child(1).text()

                val mailTemp = it.child(2).text()

                // once there was an exception when the name was empty
                if (mailTemp.length == 9)
                {
                    vicDTO.maleName = ""
                    vicDTO.maleId = mailTemp.substring(1, 8)
                }
                else
                {
                    vicDTO.maleName = mailTemp.substring(0, mailTemp.indexOf("(") - 1)
                    vicDTO.maleId = mailTemp.substring(mailTemp.indexOf("(") + 1, mailTemp.indexOf(")"))
                }



                val femaleTemp = it.child(3).text()
                vicDTO.femaleName = femaleTemp.substring(0, femaleTemp.indexOf("(") - 1)
                vicDTO.femaleId = femaleTemp.substring(femaleTemp.indexOf("(") + 1, femaleTemp.indexOf(")"))

                vicDTO.orderDate = it.child(4).text();

                val statusLink = it.child(5).toString().substring(
                        //            row.child(5).toString().indexOf("\"") + 1,
                        it.child(5).toString().indexOf("m") + 1,
                        it.child(5).toString().lastIndexOf("\""))








/*                val statusDirtyPage = client.get(statusLink).toFuture().get()
                val statusPage = ClientUtil.stringBodyAndClose(statusDirtyPage)
                val statusDocument = Jsoup.parse(statusPage)*/


                val statusDirtyPage = BufferedReader(InputStreamReader(VictoriaLoginForm::class.java.getResourceAsStream("/Pending.htm")))
                val statusPage: String = statusDirtyPage.readText()
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

                vicDTO
            }




    res.toStream().forEach(System.out::println)



}




































































/*
    repeat(3, { giftRows.removeAt(0) })

    val list = mutableListOf<VictoriaDTO>()
    var res = Flux.just<VictoriaDTO>()
    var u = Flux.just("")
    val b = Mono.just("x")
    u = u.concatWith(b)



//        val row = giftRows[0]
for (row in giftRows)
{
    val vicDTO = VictoriaDTO()
    vicDTO.operatorId = row.child(0).text()
    vicDTO.operatorEmail = row.child(1).text()

    val mailTemp = row.child(2).text()

    // once there was an exception when the name was empty
    if (mailTemp.length == 9)
    {
        vicDTO.maleName = ""
        vicDTO.maleId = mailTemp.substring(1, 8)
    }
    else
    {
        vicDTO.maleName = mailTemp.substring(0, mailTemp.indexOf("(") - 1)
        vicDTO.maleId = mailTemp.substring(mailTemp.indexOf("(") + 1, mailTemp.indexOf(")"))
    }





    val femaleTemp = row.child(3).text()
    vicDTO.femaleName = femaleTemp.substring(0, femaleTemp.indexOf("(") - 1)
    vicDTO.femaleId = femaleTemp.substring(femaleTemp.indexOf("(") + 1, femaleTemp.indexOf(")"))

    vicDTO.orderDate = row.child(4).text();

    val statusLink = row.child(5).toString().substring(
//            row.child(5).toString().indexOf("\"") + 1,
            row.child(5).toString().indexOf("m") + 1,
            row.child(5).toString().lastIndexOf("\""))

    val statusDirtyPage = client.get(statusLink).toFuture().get()

//    println(statusDirtyPage)
//    println(statusLink)
//    println(row.child(5))



    val statusPage = ClientUtil.stringBodyAndClose(statusDirtyPage)
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


    list.add(vicDTO)


    }

list.forEach { println(it) }*/








// some working Max's example
//                val statusDirtyPage = client.get(statusLink).toFuture().get()
/*
client.get(statusLink).flatMap {
    val statusPage = ClientUtil.stringBodyAndClose(it)
    val statusDocument = Jsoup.parse(statusPage)
    val orderItems = statusDocument.getElementsByClass("col-md-5")
    val orderRows = orderItems.select("tr")

    Flux.empty<String>()
}*/
