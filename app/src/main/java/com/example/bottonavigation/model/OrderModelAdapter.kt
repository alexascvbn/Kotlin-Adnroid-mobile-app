package com.example.bottonavigation.model

import java.util.*

data class OrderModelAdapter(var orderId: String, var user: String, var orderDate: Date, var totalPrice: Double, var status: Int,
                             var deliveryAddress: String, var productList : List<OrderParts>) {
}
