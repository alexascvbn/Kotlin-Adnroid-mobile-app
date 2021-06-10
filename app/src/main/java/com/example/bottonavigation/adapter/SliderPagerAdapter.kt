package com.example.bottonavigation.adapter

//import android.widget.ImageView
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.bottonavigation.PhotoActivity
import com.example.bottonavigation.R


// use in
// HomeFragment.kt
class SliderPagerAdapter(private val context: Context) : PagerAdapter() {

    companion object {
        const val  IMAGE_URL = "IMAGE_URL"
    }

    private var layoutInflater : LayoutInflater? = null
    private val images = arrayListOf(
        R.drawable.prom1,
        R.drawable.prom2,
        R.drawable.prom3
    )
    private val posters = arrayListOf<Int>(
        R.drawable.promotion1,
        R.drawable.promotion2,
        R.drawable.promotion3
    )


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return images.size
//        return Int.MAX_VALUE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        if (custPos > images.size-1) {
//            custPos = 0
//        }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vp = container as ViewPager
        val view = layoutInflater!!.inflate(R.layout.slider_layout, container, false)
        val image = view.findViewById<View>(R.id.image_view) as ImageView
//        Glide.with(view).load(custPos).into(image);
//        custPos++
        Glide.with(view).load(images[position]).into(image);
//        val subTitle = arrayOf(R.string.slideSubtitle1, R.string.slideSubtitle2, R.string.slideSubtitle3)

        view.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent: Intent = Intent(view.context, PhotoActivity::class.java)
                intent.putExtra(IMAGE_URL, posters[position])
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                view.context.startActivity(intent)
            }
        })
        vp.addView(view, 0)

        return view
//        return super.instantiateItem(container, custom_pos)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}
enum class Sliders(
    @DrawableRes val slideImage: Int,
    @DrawableRes val slidePoster: Int
) {
    CAR1(
        R.drawable.prom1,
        R.drawable.promotion1
    ),
    CAR2(
        R.drawable.prom2,
        R.drawable.promotion2
    ),
    CAR3(
        R.drawable.prom3,
        R.drawable.promotion3
    )
}
