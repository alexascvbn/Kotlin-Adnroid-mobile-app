package com.example.bottonavigation.model

import com.example.bottonavigation.util.Util
import com.google.firebase.database.Exclude
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class OrderModel(
    var orderId: String,
    var user: String,
    var orderDate: Date,
    var totalPrice: Double,
    var status: Int,
    var deliveryAddress: String,
    var user_id: String,
    var credits: Int
) : Serializable {
    val productList : ArrayList<OrderParts> = ArrayList()
    // default is three day after order date
    var deliveryDate : Date
    val MAX_STATUS_INDEX = 4


    init {
        val c = Calendar.getInstance()
        c.time = orderDate
        c.add(Calendar.DATE, 3)
        deliveryDate = c.time

        if (this.status > MAX_STATUS_INDEX) {
            this.status = 0
        }
    }

    fun addProduct(product : OrderParts) {
        this.productList.add(product)
    }
    fun removeProduct(product : OrderParts) {
        this.productList.remove(product)
    }
    fun getProduct(index : Int) : OrderParts {
        return this.productList[index]
    }

    @Exclude
    fun toMap() : Map<String, Any?> {
        val bpList: ArrayList<BaseParts>  = ArrayList()
        productList.forEach{
            bpList.add(it as BaseParts)
        }
        return mapOf(
            "orderDate" to Util.formatDate(orderDate),
            "deliveryAddress" to deliveryAddress,
            "status" to status,
            "totalPrice" to totalPrice,
            "partsList" to bpList.toList(),
            "credits" to credits
        )
    }
}