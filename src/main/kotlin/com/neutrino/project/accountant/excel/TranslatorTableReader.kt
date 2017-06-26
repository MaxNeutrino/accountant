package com.neutrino.project.accountant.excel

import com.neutrino.project.accountant.parser.model.Profile
import com.neutrino.project.accountant.parser.model.Site
import com.neutrino.project.accountant.parser.model.Translator
import org.apache.logging.log4j.LogManager
import org.apache.poi.ss.usermodel.*
import java.io.FileInputStream
import java.util.*

//TODO: replace in future
class TranslatorTableReader(excel: String) {

    private val logger = LogManager.getLogger(this)

    val excelTranslatorTable = WorkbookFactory.create(FileInputStream(excel))!!

    /**
     * Excel "Translator table" API
     *
     * Column A is named as "Vertical header"
     * Rows where Column A is not empty are "Horizontal header"
     * Anything else is "Body"
     *
     * @author MaxNeutrino
     * @author ProS96
     */
    inner class SheetReader(sheetName: String, private val site: Site) {

        var siteSheet: Sheet = excelTranslatorTable.getSheet(sheetName)

        private val percentRowNums: List<Int>
        private val adminRowNums: List<Int>
        private val translatorRowNums: List<Int>

        init {
            percentRowNums = getVertHeaderRowNums("Процент")
            adminRowNums = getVertHeaderRowNums("Админ")
            translatorRowNums = getVertHeaderRowNums("Переводчик")
        }

        fun getProfiles(): List<Profile> {
            val bodyRows = getBodyRowNums()
            val profiles: MutableList<Profile> = mutableListOf()

            siteSheet.forEach { row: Row ->
                if (bodyRows.contains(row.rowNum)) {
                    profiles.addAll(getProfileInfos(row))
                }
            }

            return profiles
        }

        private fun getProfileInfos(row: Row): List<Profile> {
            return row.map { cell: Cell ->
                val profileValue = cell.stringCellValue

                if (profileValue.contains("\n")) {
                    val profileInfos = profileValue.split("\n")
                    val profileName = profileInfos[0].trim()
                    val profileId = profileInfos[1].trim()
                    val translator = getVertHeaderInfo(cell.columnIndex, row, translatorRowNums).trim()

                    val admin = getVertHeaderInfo(cell.columnIndex, row, adminRowNums).trim()
                    var percent = getVertHeaderInfo(cell.columnIndex, row, percentRowNums).trim()

                    percent = if (percent == "0" || percent == "" || percent == null) "50.0" else percent

                    val profile = Profile(profileId, site)
                    profile.name = profileName
                    profile.translator = Translator(translator)
                    profile.admin = admin
                    profile.percent = percent.toDouble()

                    return@map profile
                } else {
                    return@map Profile("", cell.stringCellValue, site)
                }
            }
        }

        private fun getVertHeaderInfo(columnNum: Int, row: Row, rowNums: List<Int>): String {
            if (rowNums.isNotEmpty()) {
                Collections.sort(rowNums)
                Collections.reverse(rowNums)

                for (num: Int in rowNums) {

                    if (row.rowNum > num) {

                        val cell = siteSheet
                                .getRow(num)
                                .getCell(columnNum)

                        if (cell.cellTypeEnum == CellType.STRING) {
                            return cell
                                    .stringCellValue
                                    .trim()
                        } else {
                            return cell
                                    .numericCellValue
                                    .toString()
                                    .trim()
                        }
                    }
                }
            }

            return ""
        }

        private fun getVertHeaderRowNums(verticalHeaderName: String): List<Int> {
            val vertHeaderRowNums: MutableList<Int> = mutableListOf()

            siteSheet.forEach { row: Row ->
                if (row.getCell(0) != null) {
                    val cellValue = row.getCell(0)
                            .stringCellValue
                            .trim()

                    if (cellValue == verticalHeaderName) {
                        vertHeaderRowNums.add(row.rowNum)
                    }
                }
            }

            return vertHeaderRowNums
        }

        private fun getBodyRowNums(): List<Int> = siteSheet
                .filter { it.getCell(0) == null }
                .map { it.rowNum }
    }
}