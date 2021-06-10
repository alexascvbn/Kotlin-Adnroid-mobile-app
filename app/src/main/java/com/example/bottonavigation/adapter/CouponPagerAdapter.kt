package com.example.bottonavigation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.example.bottonavigation.R
import com.example.bottonavigation.adapter.CardAdapter.Companion.MAX_ELEVATION_FACTOR
import com.example.bottonavigation.model.CouponItem


class CouponPagerAdapter : PagerAdapter(), CardAdapter {
    private val mViews : ArrayList<CardView?> = ArrayList()
    private val mData : ArrayList<CouponItem> = ArrayList()
    private var mBaseElevation : Float = 0.0f

    private lateinit var mCouponClickListener: OnCouponClickListener


    fun addCouponItem(item : CouponItem) {
        mViews.add(null)
        mData.add(item)
    }
    fun removeCouponItem(item : CouponItem) {
        mViews.remove(null)
        mData.remove(item)
    }

    override fun getBaseElevation() : Float {
        return mBaseElevation
    }

    override fun getCardViewAt(position: Int): CardView {
        return mViews[position]!!
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.coupon_adapter, container, false)
        container.addView(view)
        bind(mData[position], view)

        val cardView = view.findViewById<CardView>(R.id.cardView)

        if (this.mBaseElevation == 0.0f) {
            mBaseElevation = cardView.cardElevation
        }

        cardView.maxCardElevation = mBaseElevation * MAX_ELEVATION_FACTOR
        mViews.set(position, cardView)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
        mViews[position] = null
    }


    fun SetOnCouponClickListener(mCouponClickListener: OnCouponClickListener) {
        this.mCouponClickListener = mCouponClickListener
    }

    interface OnCouponClickListener {
        fun onCouponClick(view: View?, items : ArrayList<CouponItem>, item: CouponItem)
    }

    private fun bind(item : CouponItem, view : View) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val btnGetCoupon = view.findViewById<Button>(R.id.btnGetCoupon)
        tvTitle.text = item.mTitle
        tvContent.text = item.mText
        btnGetCoupon.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                mCouponClickListener.onCouponClick(v, mData, item)
            }
        })
    }

}