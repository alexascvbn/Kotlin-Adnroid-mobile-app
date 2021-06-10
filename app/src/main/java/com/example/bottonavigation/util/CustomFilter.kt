package com.example.bottonavigation.util
//
//import android.widget.Filter
//import com.example.bottonavigation.adapter.PartsAdapter
//import com.example.bottonavigation.model.PartsModel
//
//class CustomFilter(var adapter: PartsAdapter, var filterList: ArrayList<PartsModel>) : Filter() {
//
//    override fun performFiltering(constraint: CharSequence?): FilterResults {
//        val results = FilterResults()
//        //check constraint validity
//        if (constraint != null && constraint.length > 0) {
//            val constraint = constraint.toString().toUpperCase()
//            //store our filtered models
//            val filteredModels = ArrayList<PartsModel>()
//
//            for (i in 0..filterList.size) {
//                if (filterList.get(i).title.toUpperCase().contains(constraint)) {
//                    //add Models to filtered models
//                    filteredModels.add(filterList.get(i))
//                }
//            }
//            results.count = filteredModels.size
//            results.values = filterList
//        } else {
//            results.count = filterList.size
//            results.values = filterList
//        }
//
//        return results
//
//    }
//
//    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//        this.adapter.models = results!!.values as ArrayList<PartsModel>
//        this.adapter.notifyDataSetChanged()
//
//
//    }
//
//}