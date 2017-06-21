package com.neutrino.project.accountant.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter


object DateUtil {
    val DAY = DateTimeFormatter.ofPattern("dd")
    val MONTH = DateTimeFormatter.ofPattern("MM")
    val YEAR = DateTimeFormatter.ofPattern("yyyy")

    val DAY_YEAR_BY_SLASH = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val MONTH_DAY_YEAR_BY_SLASH = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val DAY_YEAR_BY_DOT = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val YEAR_DAY_BY_DASH = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun day(date: LocalDate) = date.format(DAY)
    fun month(date: LocalDate) = date.format(MONTH)
    fun year(date: LocalDate) = date.format(YEAR)
}