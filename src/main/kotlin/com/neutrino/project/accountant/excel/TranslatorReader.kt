package com.neutrino.project.accountant.excel

import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.parser.model.Statistic
import com.neutrino.project.accountant.parser.model.Translator
import com.neutrino.project.accountant.util.exception.ExcelParseException
import org.apache.logging.log4j.LogManager
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import reactor.core.publisher.Flux
import java.io.FileInputStream

/**
 * Read translator table data
 * Will be replace in future
 */
class TranslatorReader(excel: String) {

    private val logger = LogManager.getLogger(this)

    val wb = WorkbookFactory.create(FileInputStream(excel))
    var sheet: Sheet? = null

    fun readSheet(sheetName: String) {
        logger.info("readSheet $sheetName")
        sheet = wb.getSheet(sheetName)
    }

    fun readPosition(cellValue: String): List<Int> {
        val rows: MutableList<Int> = mutableListOf()

        sheet!!.forEach { row: Row ->
            if (row.getCell(0) != null)
                if (row.getCell(0).stringCellValue == cellValue)
                    rows.add(row.rowNum)
        }

        return rows
    }


    fun readTranslator(statistics: Flux<Statistic>): Flux<Statistic> {
        logger.info("readTranslator")
        val translatorsName = readPosition("Переводчик")

        return statistics.map {

            if (it.profile.translator != null && it.profile.translator!!.name?.isNotEmpty()!!)
                return@map it

            var nameValue: String? = if (it.profile.surname == null) it.profile.name else it.profile.surname

            nameValue = nameValue ?: ""

            if (it.profile.site == Site.NATASHA)
                nameValue = it.profile.siteId

            var translatorName = readTranslator(nameValue, translatorsName)
            translatorName = if (translatorName == "Переводчик") "" else translatorName

            val translator = Translator(translatorName)
            it.profile.translator = translator

            return@map it
        }
    }

    fun readTranslator(name: String, translatorPosition: List<Int>): String {
        val num = readTranslator(name)
        val rowNum = num.second
        val cellNum = num.first

        if (translatorPosition.size > 1) {
            var pos = 0

            translatorPosition.forEach {
                if (rowNum > it) {
                    pos = it
                } else {
                    return@forEach
                }
            }

            return sheet!!
                    .getRow(pos)
                    .getCell(cellNum)
                    .stringCellValue
        } else {
            val pos = translatorPosition.first()
            return sheet!!
                    .getRow(pos)
                    .getCell(cellNum)
                    .stringCellValue
        }
    }

    private fun readTranslator(name: String): Pair<Int, Int> {

        try {
            if (name.contains(" ")) {
                val splitedName: List<String> = name.split(" ")

                try {
                    if (splitedName.size == 2) {
                        return profilePosition(splitedName[0])

                    } else if (splitedName.size == 3) {
                        return profilePosition(splitedName[2])
                    }

                } catch(e: ExcelParseException) {
                    if (splitedName.size == 2) {
                        return profilePosition(splitedName[1])

                    } else if (splitedName.size == 3) {
                        return profilePosition(splitedName[2])
                    }

                }

            } else {
                return profilePosition(name)
            }

        } catch(e: ExcelParseException) {
            return Pair(0, 0)
        }

        return Pair(0, 0)
    }

    private fun profilePosition(name: String): Pair<Int, Int> {
        val confirmedName = name.replace("\n", "").trim()
        var cellPosition: Int = 0
        var rowPosition: Int = 0
        var containsCounter: Int = 0
        val deadZone = readDeadZones()

        sheet!!
                .filter { !isDead(deadZone, it) }
                .forEach { row ->

                    row.forEach { cell ->

                        if (cell.stringCellValue.contains(confirmedName)) {
                            containsCounter++

                            if (containsCounter > 1)
                                throw ExcelParseException()
                            else {
                                cellPosition = cell.columnIndex
                                rowPosition = row.rowNum
                            }
                        }
                    }
                }

        return Pair(cellPosition, rowPosition)
    }

    private fun readDeadZones(): List<Int> = sheet!!
            .filter { it.getCell(0) != null }
            .map { it.rowNum }

    private fun isDead(deadList: List<Int>, row: Row): Boolean = deadList.contains(row.rowNum)
}