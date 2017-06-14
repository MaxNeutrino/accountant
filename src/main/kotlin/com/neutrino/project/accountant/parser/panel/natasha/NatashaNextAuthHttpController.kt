package com.neutrino.project.accountant.parser.panel.natasha

import com.neutrino.project.accountant.client.ResponseListener


class NatashaNextAuthHttpController(httpController: NatashaHttpController) : NatashaHttpController(httpController.client) {

    override fun requestAndSubscribe(credential: Map<String, String>, listener: ResponseListener) {
        auth("/index.php", credential, listener)
    }
}