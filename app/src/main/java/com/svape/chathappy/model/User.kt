package com.svape.chathappy.model

class User {
    private var uid : String = ""
    private var nameUser : String = ""
    private var email : String = ""
    private var numberPhone : String = ""
    private var image : String = ""
    private var search : String = ""
    private var names : String = ""
    private var lastName : String = ""
    private var age : String = ""
    private var profession : String = ""
    private var address : String = ""

    constructor()

    constructor(
        uid: String,
        nameUser: String,
        email: String,
        numberPhone: String,
        image: String,
        search: String,
        names: String,
        lastName: String,
        age: String,
        profession: String,
        address: String
    ) {
        this.uid = uid
        this.nameUser = nameUser
        this.email = email
        this.numberPhone = numberPhone
        this.image = image
        this.search = search
        this.names = names
        this.lastName = lastName
        this.age = age
        this.profession = profession
        this.address = address
    }

    //getters y setters
    fun getUid() : String?{
        return uid
    }

    fun setUid(uid : String){
        this.uid = uid
    }

    fun getNameUser() : String?{
        return nameUser
    }

    fun setNameUser(nameUser : String){
        this.nameUser = nameUser
    }

    fun getEmail() : String?{
        return email
    }

    fun setEmail(email : String){
        this.email = email
    }

    fun getNumberPhone() : String?{
        return numberPhone
    }

    fun setNumberPhone(numberPhone : String){
        this.numberPhone = numberPhone
    }

    fun getImage() : String?{
        return image
    }

    fun setImage(image : String){
        this.image = image
    }

    fun getSearch() : String?{
        return search
    }

    fun setSearch(search : String){
        this.search = search
    }

    fun getNames() : String?{
        return names
    }

    fun setNames(names : String){
        this.names = names
    }

    fun getLastName() : String?{
        return lastName
    }

    fun setLastName(lastName : String){
        this.lastName = lastName
    }

    fun getAge() : String?{
        return age
    }

    fun setAge(age : String){
        this.age = age
    }

    fun getProfession() : String?{
        return profession
    }

    fun setProfession(profession : String){
        this.profession = profession
    }

    fun getAddress() : String?{
        return address
    }

    fun setAddress(address : String){
        this.address = address
    }
}