package com.neutrino.project.accountant.client.parser

import reactor.core.publisher.Mono


interface MonoHtmlParser<T>: Parser<String, Mono<T>>