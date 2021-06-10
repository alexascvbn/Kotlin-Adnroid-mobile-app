package com.example.bottonavigation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottonavigation.adapter.CartAdapter
import com.example.bottonavigation.database.LocalDatabase
import com.example.bottonavigation.model.Order
import com.example.bottonavigation.model.OrderParts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import info.hoang8f.widget.FButton
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class CartActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    lateinit var database: FirebaseDatabase
    lateinit var request: DatabaseReference

    var txtTotalPrice: TextView? = null
    var btnPlace: FButton? = null

    var cart: ArrayList<OrderParts> = ArrayList()
    var adapter: CartAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        database = FirebaseDatabase.getInstance()
        request = database.getReference("Request")

        recyclerView = findViewById(R.id.listCart)
        recyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager

        txtTotalPrice = findViewById(R.id.total)
        btnPlace = findViewById(R.id.btnPlaceOrder)

        LoadListFood()

    }

    private fun LoadListFood() {
        cart = LocalDatabase(this).getCarts()
        adapter = CartAdapter(cart,this)
        recyclerView?.adapter = adapter

        var total = 0
        for (order in cart) {
            total += order.price.toInt() + order.qty
            val locale = Locale("en","US")
            val fmt = NumberFormat.getCurrencyInstance(locale)

            txtTotalPrice?.setText(fmt.format(total))
        }


    }
}
