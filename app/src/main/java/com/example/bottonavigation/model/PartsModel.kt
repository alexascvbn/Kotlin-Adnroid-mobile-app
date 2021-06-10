package com.example.bottonavigation.model

class PartsModel {
    var id: String
    var categoryId: Int
    var name: String
    var newOrOld: String
    var suitableCar: String
    var description: String
    var price: Double
    var image: String
    var discount: String

    constructor() : this("", 0, "", "", "", "", 0.0, "", "")

    constructor(
        id: String,
        categoryID: Int,
        name: String,
        newOrOld: String,
        suitableCar: String,
        description: String,
        price: Double,
        image: String,
        discount: String
    ) {
        this.id = id
        this.categoryId = categoryID
        this.name = name
        this.newOrOld = newOrOld
        this.suitableCar = suitableCar
        this.description = description
        this.price = price
        this.discount = discount
        this.image = image
    }


    fun toOrderParts() : OrderParts {
        val op = OrderParts(this.categoryId, this.id, this.name, this.price, 0, this.image, this.description)
        return op
    }
}
