package com.neutrino.project.accountant.client.panel.victoria

import java.time.LocalDate

fun main(args: Array<String>) {
    val b = DIDIT()

    val a = LocalDate.of(2017, 6, 1)
    val c = LocalDate.of(2017, 6, 10)

    b.getVictoriaDTOs(a, c).subscribe { println(it) }
}