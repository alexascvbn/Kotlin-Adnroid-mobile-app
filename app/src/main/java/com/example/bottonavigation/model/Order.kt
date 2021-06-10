package com.example.bottonavigation.model

class Order {
    var productId: String
    var productName: String
    var Quantity: Int
    var Price: Double
    var Discount: String

    constructor() : this("0", "", 0, 0.0, "")


    constructor(
        productId: String,
        productName: String,
        Quantity: Int,
        Price: Double,
        Discount: String
    ) {
        this.productId = productId
        this.productName = productName
        this.Quantity = Quantity
        this.Price = Price
        this.Discount = Discount
    }


}