package com.example.bottonavigation.bottomFragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.bottonavigation.MainActivity
import com.example.bottonavigation.PartsDetailActivity
import com.example.bottonavigation.sidebarFragment.SinglePartFragment
import com.example.bottonavigation.R
import com.example.bottonavigation.adapter.NewsListAdapter
import com.example.bottonavigation.adapter.SliderPagerAdapter
import com.example.bottonavigation.model.NewsModel
import com.example.bottonavigation.sidebarFragment.PdfViewFragment
import com.example.bottonavigation.sidebarFragment.ProfileFragment
import com.example.bottonavigation.ui.login.LoginActivity2
import com.example.bottonavigation.util.CircularViewPagerHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var slidePager: ViewPager
    private lateinit var adapter: SliderPagerAdapter
    private lateinit var timer: Timer
    private lateinit var btnTest: Button
    private lateinit var btnLogin: Button
    private lateinit var cvHitProduct1: CardView
    private lateinit var cvHitProduct2: CardView
    private lateinit var hit_p1_img: ImageView
    private lateinit var logRegLayout : RelativeLayout

    private var curr_pos: Int = 0
    private var run_count: Int = 0
    lateinit var storage: FirebaseStorage
    private val newsList: ArrayList<NewsModel> = ArrayList()
    private lateinit var news_recycler: RecyclerView
    private lateinit var newsAdapter: NewsListAdapter
    private lateinit var logRegBar : RelativeLayout

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    var myRef: DatabaseReference = database.getReference("news")
    private val handler: Handler = Handler()
    private val runnable: Runnable = Runnable {
        curr_pos = slidePager.currentItem
        if (curr_pos == adapter.count - 1) {
            curr_pos = -1
        }
//            Log.d("johnson***", "time : " + run_count.toString())
        run_count++
//            Log.d("johnson***", "current pos " + curr_pos.toString())
        slidePager.setCurrentItem(++curr_pos, true)
    }

    companion object {
        const val RESULT_FOR_LOGIN = 666
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        slidePager = view.findViewById(R.id.slide_view_pager)
        news_recycler = view.findViewById(R.id.news_recycler)
        cvHitProduct1 = view.findViewById(R.id.hit_product1)
        cvHitProduct1.setOnClickListener(this)
        cvHitProduct2 = view.findViewById(R.id.hit_product2)
        cvHitProduct2.setOnClickListener(this)

        logRegBar = view.findViewById<RelativeLayout>(R.id.logRegBar)

        newsList.clear()

        newsList.add(NewsModel(resources.getString(R.string.home_news), ArrayList<NewsModel>()))
        val myRef = database.getReference("news")
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val imgId = arrayOf(R.drawable.car1, R.drawable.car2, R.drawable.car3)
                val singleNewsList: ArrayList<NewsModel> = ArrayList()

                newsList[0].clearNews()
                var carI = 0
                for (ds: DataSnapshot in dataSnapshot.children) {
                    val typeId = ds.child("typeId").getValue(Int::class.java)!!

                    var model : NewsModel? = null

                    if (typeId == 1) {
                        model = NewsModel(1,
                            ds.child("title").getValue(String::class.java)!!,
                            getURLForResource(imgId[carI]),
                            ds.child("pdf_url").getValue(String::class.java)!!
                        )
                    }
                    else if (typeId == 2) {
                        model = NewsModel(2,
                            ds.child("title").getValue(String::class.java)!!,
                            getURLForResource(imgId[carI]),
                            ds.child("site_url").getValue(String::class.java)!!
                        )
                    }

                    if (++carI == imgId.size) {
                        carI = 0
                    }
                    model?.let { singleNewsList.add(it) }

                }
                newsList[0].singleItemList = singleNewsList
//                newsList.add(NewsModel(resources.getString(R.string.home_news), singleNewsList))
                newsAdapter.updateList(newsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("HomeFragment", "Failed to read value.", error.toException())
            }
        })

        btnTest = view.findViewById(R.id.btnTest)
        btnTest.setOnClickListener(this)

        btnLogin = view.findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener(this)

        logRegLayout = view.findViewById(R.id.logRegBar)

        slidePager.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.action == MotionEvent.ACTION_DOWN && v is ViewGroup) {
                    v.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }
        });
        slidePager.addOnPageChangeListener(CircularViewPagerHandler(slidePager))
        adapter =
            SliderPagerAdapter(context!!.applicationContext)

        slidePager.adapter = adapter
        val springDotsIndicator = view.findViewById<SpringDotsIndicator>(R.id.spring_dots_indicator)
        springDotsIndicator.setViewPager(slidePager)

        setAdapter()
        createSlideShow()
//        arrayOf(R.raw.geniuepart, R.raw.news1, R.raw.news2, R.raw.news3, R.raw.news4).forEach {
//            Log.d("johnson***", getURLForResource(it))
//        }


        return view
    }


    fun createSlideShow() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, 5000, 5000)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTest -> {
//                val url = "https://developers.google.com/admob/android/banner"
//                val customTabsIntent = CustomTabsIntent.Builder().build()
//                customTabsIntent.launchUrl(activity , Uri.parse(url))
            }
            R.id.btnLogin -> {
                val intent = Intent(this.context, LoginActivity2::class.java)
                startActivityForResult(intent, RESULT_FOR_LOGIN)
            }
            R.id.hit_product1 -> {
//                val frag =
//                    SinglePartFragment()
//                val ft = fragmentManager?.beginTransaction()
//                ft?.replace(R.id.drawer_layout, frag, "singlePartFragment")
//                    ?.addToBackStack("singlePartFragment")
//                    ?.commit()
                val partsDatail = Intent(context, PartsDetailActivity::class.java)
                partsDatail.putExtra("PartId", "0")
                startActivity(partsDatail)
            }
            R.id.hit_product2 -> {
                val partsDatail = Intent(context, PartsDetailActivity::class.java)
                partsDatail.putExtra("PartId", "1")
                startActivity(partsDatail)
            }
        }
    }

    private fun setAdapter() {

        news_recycler.setHasFixedSize(true)
        news_recycler.layoutManager = LinearLayoutManager(context)

        newsAdapter = NewsListAdapter(context, newsList)
        news_recycler.adapter = newsAdapter
        // Log.d("johnson***", "newsList" + newsList.size.toString())
        val singleList = ArrayList<NewsModel>()
        for (i in 0..2) {
            singleList.add(NewsModel(0, "Loading", getURLForResource(R.drawable.gradient_fab), ""))
        }
        newsList[0].singleItemList = singleList

        newsAdapter.SetOnMoreClickListener(object : NewsListAdapter.OnMoreClickListener {
            override fun onMoreClick(view: View?, position: Int, model: NewsModel?) {
                // replace to fragment grid view fragment
                Toast.makeText(context, "function still not implement", Toast.LENGTH_SHORT).show()
            }
        })
        newsAdapter.SetOnItemClickListener(object : NewsListAdapter.OnItemClickListener {
            override fun onItemClick(
                view: View?,
                absolutePosition: Int,
                relativePosition: Int,
                model: NewsModel?
            ) {
                if (model?.source_url != null) {
                    if (model.typeId == 1) {
                        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawer_layout)
                        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                        val frag = PdfViewFragment.newInstance(model.source_url)
                        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
                        ft.replace(R.id.drawer_layout, frag!!, "pdfViewFragment")
                            .addToBackStack("pdfViewFragment")
                            .commit()
                    }
                    else if (model.typeId == 2) {
                        val customTabsIntent = CustomTabsIntent.Builder().build()
                        customTabsIntent.launchUrl(activity , Uri.parse(model.source_url))
                    }
                } else {
                    Toast.makeText(context, "source is null, maybe deleted", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("johnson***", model?.source_url)
                }
            }
        })

    }

    override fun onPause() {
        timer.cancel()
//        newsList.clear()
        super.onPause()
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    fun getURLForResource(resId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().getName() + "/" + resId)
            .toString()
    }


    override fun onResume() {
        super.onResume()
        if (auth.currentUser != null) {
            logRegBar.visibility = View.GONE
        }
        else {
            logRegBar.visibility = View.VISIBLE
        }
    }
}

