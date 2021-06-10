package com.example.bottonavigation.model

data class UserModel(var userId: String, var email: String, var password: String) {
    var address : String? = null
    var deliveryAddress : String? = null
    var company : String? = null
}
