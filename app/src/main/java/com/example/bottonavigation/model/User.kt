package com.example.bottonavigation.model

class User {
    var name: String
    var password: String
    var phone: String


    constructor(name: String, password: String, phone: String) {
        this.name = name
        this.password = password
        this.phone = phone
    }
}