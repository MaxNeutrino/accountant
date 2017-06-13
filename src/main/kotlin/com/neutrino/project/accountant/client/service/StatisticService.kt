package com.neutrino.project.accountant.client.service

import com.neutrino.project.accountant.client.model.Statistic
import reactor.core.publisher.Flux


interface StatisticService {

    fun statistic(): Flux<Statistic>
}