package com.example.bottonavigation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottonavigation.holder.PartsHolder
import com.example.bottonavigation.model.BranchModel
import com.example.bottonavigation.model.PartsModel
//import com.example.bottonavigation.util.CustomFilter

class PartsAdapter(
    var c: Context? = null,
    var models: ArrayList<PartsModel>,
    private val layoutManager: GridLayoutManager? = null
) :
    RecyclerView.Adapter<PartsHolder>(), Filterable {

    private var filterList: ArrayList<PartsModel> = models
//    private lateinit var filter: CustomFilter
    var btn_addToCart: Button? = null

    override fun getItemCount(): Int {
        return models.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, model: BranchModel?)
    }

    override fun onBindViewHolder(holder: PartsHolder, position: Int) {
        //bind data to our views
        holder.bind(models[position])

        //animation
        val animation: Animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)

    }

    override fun getFilter(): Filter {
//        if (filter == null) {
//            filter = CustomFilter(this, filterList)
//        }
        return filter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartsHolder {
        TODO("Not yet implemented")
    }

}