package com.example.bottonavigation.sidebarFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.bottonavigation.R
import com.example.bottonavigation.adapter.CouponPagerAdapter
import com.example.bottonavigation.model.CouponItem
import com.example.bottonavigation.util.ShadowTransformer


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SinglePartFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SinglePartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CouponFragment : BaseSideFragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename and change types of parameters
    override val titleId: Int = R.string.menu_coupon
//    private val mButton by bindView<Button>(R.id.cardTypeBtn)
    private val mViewPager by bindView<ViewPager>(R.id.viewPager)
    private val headerImg by bindView<ImageView>(R.id.handsomeImg)
//    private val cbScale by bindView<CheckBox>(R.id.checkBox)

    private lateinit var mCouponAdapter : CouponPagerAdapter
    private lateinit var mCardShadowTransformer : ShadowTransformer
//    private lateinit var mFragmentPagerAdapter : CouponCardFragmentPagerAdapter
//    private lateinit var mFragmentCardShadowTransformer : ShadowTransformer
    private var mShowingFragments : Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.side_fragment_coupon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mButton.setOnClickListener(this)
//        cbScale.setOnCheckedChangeListener(this)
        mCouponAdapter = CouponPagerAdapter()

        mCouponAdapter.addCouponItem(CouponItem("add 50 credit point!!!", "Very Cheap discount"))
        mCouponAdapter.addCouponItem(CouponItem("delete 50 credit point!!!", "Very expensive discount"))
//        mFragmentPagerAdapter = CouponCardFragmentPagerAdapter(fragmentManager!!,
//            dpToPixels(2, context!!))
        mCouponAdapter.SetOnCouponClickListener(object : CouponPagerAdapter.OnCouponClickListener{
            override fun onCouponClick(view: View?, items: ArrayList<CouponItem>, item: CouponItem) {
                val coupon = items[mViewPager.currentItem]
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Coupon redeemed")
                    .setMessage("You can use it to \n" + coupon.mTitle)
                    .setPositiveButton(R.string.OK) { dialog, which ->
                        run {
                            mCouponAdapter.removeCouponItem(item)
                            mCouponAdapter.notifyDataSetChanged()
                            dialog?.cancel()
                        }
                    }

                val dialog = builder.create()
                dialog.show()

            }
        })

        mCardShadowTransformer = ShadowTransformer(mViewPager, mCouponAdapter)
//        mFragmentCardShadowTransformer = ShadowTransformer(mViewPager, mFragmentPagerAdapter)

        mViewPager.adapter = mCouponAdapter
        mViewPager.setPageTransformer(false, mCardShadowTransformer)
        mViewPager.offscreenPageLimit = 3
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            var currentPage : Int = 0
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                this.currentPage = position
                val imgList = arrayOf(R.drawable.car1, R.drawable.car2)
                context?.let {
                    Glide.with(context!!).load(imgList[position]).into(headerImg)
                }
            }

            fun getCurrentIndex() : Int? {
                return currentPage
            }

        })
    }




    private fun dpToPixels(dp: Int, context: Context): Float {
        return dp * context.getResources().getDisplayMetrics().density
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//        mCardShadowTransformer.enableScaling(isChecked)
//        mFragmentCardShadowTransformer.enableScaling(isChecked)
    }

    override fun onClick(v: View?) {
        if (!mShowingFragments) {
//            mButton.text = "Views";
//            mViewPager.adapter = mFragmentPagerAdapter;
//            mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        } else {
//            mButton.text = "Fragments";
            mViewPager.adapter = mCouponAdapter;
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
        }

        mShowingFragments = !mShowingFragments

    }
}
