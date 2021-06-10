package com.example.bottonavigation.adapter

import androidx.cardview.widget.CardView

interface CardAdapter {
    companion object {
        val MAX_ELEVATION_FACTOR : Int = 8
    }

    fun getBaseElevation() : Float
    fun getCardViewAt(pos : Int) : CardView
    fun getCount() : Int
}