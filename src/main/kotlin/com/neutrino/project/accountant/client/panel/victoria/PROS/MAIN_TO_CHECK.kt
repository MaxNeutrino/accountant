package com.neutrino.project.accountant.client.panel.victoria.PROS

fun main(args: Array<String>) {
    val b = IT_WORKS()

    val a = java.time.LocalDate.of(2017, 6, 1)
    val c = java.time.LocalDate.of(2017, 6, 10)

    b.getVictoriaDTOs(a, c).subscribe { println(it) }
}