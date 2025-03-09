package com.svape.chathappy.model

class Chat {
    private var idMessage : String = ""
    private var transmitter : String = ""
    private var receptor : String = ""
    private var message : String = ""
    private var url : String = ""
    private var view = false

    constructor()

    constructor(
        idMessage: String,
        transmitter: String,
        receptor: String,
        message: String,
        url: String,
        view: Boolean
    ) {
        this.idMessage = idMessage
        this.transmitter = transmitter
        this.receptor = receptor
        this.message = message
        this.url = url
        this.view = view
    }

    //getters y setters
    fun getIdMessage() : String?{
        return idMessage
    }

    fun setIdMessage(idMessage : String?){
        this.idMessage = idMessage!!
    }

    fun getTransmitter() : String?{
        return transmitter
    }

    fun setTransmitter(transmitter : String?){
        this.transmitter = transmitter!!
    }

    fun getReceptor() : String?{
        return receptor
    }

    fun setReceptor(receptor : String?){
        this.receptor = receptor!!
    }

    fun getMessage() : String?{
        return message
    }

    fun setMessage(message : String?){
        this.message = message!!
    }

    fun getUrl() : String?{
        return url
    }

    fun setUrl(url : String?){
        this.url = url!!
    }

    fun isView() : Boolean{
        return view
    }

    fun setIsView(view : Boolean?){
        this.view = view!!
    }
}