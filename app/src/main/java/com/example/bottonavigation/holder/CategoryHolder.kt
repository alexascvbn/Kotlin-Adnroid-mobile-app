package com.example.bottonavigation.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottonavigation.R
import com.example.bottonavigation.listener.ItemClickListener
import com.example.bottonavigation.model.CategoryModel

class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var txtCategoryName: TextView
    var imageCategory: ImageView
    private lateinit var itemClickListener: ItemClickListener

    init {
        super.itemView
        this.txtCategoryName = itemView.findViewById(R.id.category_name)
        this.imageCategory = itemView.findViewById(R.id.category_image)
        itemView.setOnClickListener(this)
    }

    fun bind(model : CategoryModel) {
        this.txtCategoryName.text = model.name
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    override fun onClick(v: View?) {
        itemClickListener.onItemClick(v!!, pos = adapterPosition)
    }

}