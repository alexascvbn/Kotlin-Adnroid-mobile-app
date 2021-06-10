package com.example.bottonavigation.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.example.bottonavigation.R
import com.example.bottonavigation.listener.ItemClickListener
import com.example.bottonavigation.model.Order
import com.example.bottonavigation.model.OrderParts
import kotlinx.android.synthetic.main.layout_cart.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


    var txt_cart_name: TextView? = null
    var txt_price: TextView? = null
    var img_cart_count: ImageView? = null

    init {
        super.itemView
        txt_cart_name = itemView.findViewById(R.id.cart_item_name)
        txt_price = itemView.findViewById(R.id.cart_item_price)
        img_cart_count = itemView.findViewById(R.id.cart_item_count)
    }

    val itemClickListener: ItemClickListener? = null

//    fun setTxt_cart_name(txt_cart_name : TextView){
//        this.txt_cart_name = txt_cart_name
//    }


    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


class CartAdapter(listData: ArrayList<OrderParts>, context: Context?) :
    RecyclerView.Adapter<CartViewHolder>() {
    var listData: ArrayList<OrderParts> = ArrayList()
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_cart,
                parent,
                false
            )
        )


    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        var drawable: TextDrawable = TextDrawable.builder().buildRound(
            "" + listData.get(position).qty,
            Color.RED
        )
        holder.img_cart_count?.setImageDrawable(drawable)

        var locale = Locale("en", "US")
        var fmt = NumberFormat.getCurrencyInstance(locale)
        var price =
            (listData.get(position).price).toInt() * listData.get(position).qty
        holder.txt_price?.setText(fmt.format(price))
        holder.txt_cart_name?.setText(listData.get(position).name)
    }

}