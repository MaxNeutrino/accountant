package com.neutrino.project.accountant.util.exception


class BadCredentialException: Exception {


    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    constructor() : super()
}