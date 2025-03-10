package com.svape.chathappy.model

data class Chat(
    var idMessage: String = "",
    var messageID: String = "",
    var transmitter: String = "",
    var receptor: String = "",
    var receiver: String = "",
    var message: String = "",
    var url: String = "",
    var view: Boolean = false
)
