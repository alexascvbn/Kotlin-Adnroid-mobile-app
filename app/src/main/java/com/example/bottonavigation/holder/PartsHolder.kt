package com.example.bottonavigation.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottonavigation.R
import com.example.bottonavigation.listener.ItemClickListener
import com.example.bottonavigation.model.PartsModel

class PartsHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    //views
    var part_id: TextView
    var part_name: TextView
    var part_price: TextView
    var part_image: ImageView
    var part_description: TextView
    private lateinit var itemClickListener: ItemClickListener

    init {
        super.itemView
        this.part_id = itemView.findViewById(R.id.part_id)
        this.part_name = itemView.findViewById(R.id.part_name)
        this.part_price = itemView.findViewById(R.id.part_price)
        this.part_image = itemView.findViewById(R.id.part_image)
        this.part_description = itemView.findViewById(R.id.part_description)
        itemView.setOnClickListener(this)
    }

    constructor(parent:ViewGroup):this(LayoutInflater.from(parent.context).inflate(R.layout.parts_item, parent,false))


    fun bind(model: PartsModel) {
        this.part_id.text = model.id
//        this.part_cat?.text = String.format("%d", model.categoryID)
        this.part_name.text = model.name
        this.part_price.text = "$ " + model.price
        this.part_description.text = model.description
//        this.mImageIv?.setImageResource(model.image)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onClick(v: View?) {
        itemClickListener.onItemClick(v!!, pos = adapterPosition)
    }
}