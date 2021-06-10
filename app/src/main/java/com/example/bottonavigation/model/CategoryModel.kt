package com.example.bottonavigation.model

import android.widget.ImageView

class CategoryModel {
    var id: Int
    var name: String
    var image: String

    constructor() : this(0, "", "") {

    }

    constructor(id: Int, name: String, image: String) {
        this.id = id
        this.name = name
        this.image = image
    }
}