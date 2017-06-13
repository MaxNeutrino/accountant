package com.neutrino.project.accountant.client.parser

import reactor.core.publisher.Flux


interface HtmlParser<T>: Parser<String, Flux<T>>