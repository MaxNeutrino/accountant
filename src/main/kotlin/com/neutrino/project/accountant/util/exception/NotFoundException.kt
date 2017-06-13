package com.neutrino.project.accountant.util.exception


class NotFoundException : Exception {

    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}