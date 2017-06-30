package com.neutrino.project.accountant.excel

import com.neutrino.project.accountant.parser.model.Statistic
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import reactor.core.publisher.Flux
import java.io.FileOutputStream

/**
 * Write statistic to excel book
 * Will be replaced in future
 */
class StatisticWriter {

    val wb = XSSFWorkbook()
    val sheet = wb.createSheet("Salary")

    private fun createHeader() {
        val header = sheet.createRow(0)

        header.createCell(0).setCellValue("ID клиентки")
        header.createCell(1).setCellValue("ФИО клиентки")
        header.createCell(2).setCellValue("Переводчик")
        header.createCell(3).setCellValue("Админ")
        header.createCell(4).setCellValue("Админка")
        header.createCell(5).setCellValue("Заработки")
        header.createCell(6).setCellValue("Процент")
    }

    fun write(statistic: Flux<Statistic>) {
        createHeader()
        var rowCount: Int = 1

        statistic.subscribe {
            val row = sheet.createRow(rowCount)

            saveCell(row, 0, checker(it.profile.siteId))
            saveCell(row, 1, "${checker(it.profile.surname)} ${checker(it.profile.name)}")
            saveCell(row, 2, "${checker(it.profile.translator?.name)} ${checker(it.profile.translator?.surname)}")
            saveCell(row, 3, checker(it.profile.admin))
            saveCell(row, 4, checker(it.panelName))
            saveCell(row, 5, it.pay)
            saveCell(row, 6, it.profile.percent.toString())

            /*row.createCell(0).setCellValue(it.profile.siteId)
            row.createCell(1).setCellValue("${it.profile.surname} ${it.profile.name}")
            row.createCell(2).setCellValue("${it.profile.translator?.name} ${it.profile.translator?.surname}")
            row.createCell(3).setCellValue(it.panelName)
            row.createCell(4).setCellValue(it.pay)*/

            rowCount++
        }

        write()
    }

    private fun write() {
        FileOutputStream("D:\\workbook.xls").use {
            wb.write(it)
        }
    }

    private fun saveCell(row: Row, index: Int, data: String) {
        row.createCell(index)
                .setCellValue(data)
    }

    private fun saveCell(row: Row, index: Int, data: Double) {
        row.createCell(index)
                .setCellValue(data)
    }

    private fun checker(data: String?): String = data ?: ""

}