package com.example.bottonavigation.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull

open class ArrayAdapterWithHint<T>(context: Context, resource: Int): ArrayAdapter<T>(context, resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = super.getView(position, convertView, parent)
        if (position == count) {
            (v.findViewById<TextView>(android.R.id.text1)).setText("")
            (v.findViewById<TextView>(android.R.id.text1)).setHint(getItem(count).toString())
        }
        return v
    }

    override fun getCount(): Int {
        return super.getCount() - 1
    }
}
