package com.example.bottonavigation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Binder
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class DetailActivity : AppCompatActivity() {

    lateinit var mIDTv: TextView
    lateinit var mCategorytTv : TextView
    lateinit var mTitleTv: TextView
    lateinit var mDescrTv: TextView
    lateinit var mImageIv: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //actionbar
        val actionBar: ActionBar? = supportActionBar

        mIDTv = findViewById(R.id.dIDTv)
        mCategorytTv = findViewById(R.id.dCategoryTv)
        mTitleTv = findViewById(R.id.dTitleTv)
        mDescrTv = findViewById(R.id.dDescrTv)
        mImageIv = findViewById(R.id.dImageIv)

        //Intent
        val intent: Intent = intent
        val mID : String = intent.getStringExtra("iID")
        val mTitle: String = intent.getStringExtra("iTitle")
        val mCategory: String = intent.getStringExtra("iCategory")
        val mDescr: String = intent.getStringExtra("iDescr")
//        val mBytes: ByteArray? = getIntent().getByteArrayExtra("iImage")
        //decode bytes array to bitmap
//        val bitmap: Bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes!!.size)
//        val bitmapDrawable: BitmapDrawable = ContextCompat.getDrawable(this, imageId) as BitmapDrawable
        val imageId : Int = intent.getIntExtra("iImage", R.drawable.inchcapesmalllogo)

        Glide.with(this)
            .load(imageId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .error(R.drawable.inchcapesmalllogo)
            .into(mImageIv)

        //set title to actionbar
        actionBar?.title = mTitle

        //set data to our views
        mIDTv.setText(mID)
        mTitleTv.setText(mTitle)
        mCategorytTv.setText(mCategory)
//        mImageIv.setImageBitmap(bitmap)
        mDescrTv.setText(mDescr)


    }
}