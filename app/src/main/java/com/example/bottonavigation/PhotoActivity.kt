package com.example.bottonavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import androidx.appcompat.widget.Toolbar

class PhotoActivity : AppCompatActivity() {
    lateinit var fullscreen_content: PhotoView
    lateinit var toolbar: Toolbar

    companion object {
        const val  IMAGE_URL = "IMAGE_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        toolbar = findViewById<Toolbar>(R.id.toolbar)

        fullscreen_content = findViewById<PhotoView>(R.id.fullscreen_content)
        val imageUrl : Int = intent.getIntExtra(IMAGE_URL, 0)

        Glide.with(applicationContext).load(imageUrl).into(fullscreen_content)
        fullscreen_content.maximumScale = 20F
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

//        Toast.makeText(this@PhotoActivity, imageUrl, Toast.LENGTH_SHORT).show()
    }
}
