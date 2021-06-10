package com.example.bottonavigation.bottomFragment

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bottonavigation.R
import com.example.bottonavigation.adapter.BranchListAdapter
import com.example.bottonavigation.model.BranchModel
import com.example.bottonavigation.util.ArrayAdapterWithHint
import com.example.bottonavigation.util.NDSpinner
import com.example.bottonavigation.util.RecyclerViewScrollListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class BranchFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbarInActivity: Toolbar
    private lateinit var swipeRefreshRecyclerList: SwipeRefreshLayout
    private lateinit var filterButtons: Array<Button?>
    private lateinit var btn_unfocus: Button
    private lateinit var fab: FloatingActionButton
    private lateinit var scrollListener: RecyclerViewScrollListener
    private lateinit var spBrand: NDSpinner
    private lateinit var spBranchType: NDSpinner

//    private var region_id : Int = 0
    private var regList: Array<String>? = null
    private var brand_id : Int = 0
    private var bType_id : Int = 0


    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference

    private val branchList: ArrayList<BranchModel> = ArrayList()
    private var filteredList: ArrayList<BranchModel> = ArrayList()
    private var mAdapter: BranchListAdapter? = null

    private lateinit var prevModel: BranchModel
//    private var prevLng: Double = 22.2860444
//    private var prevLat: Double = 114.1905378
//    private var prevAddress: String = "香港北角威非路道11號順興樓地下A號舖"


    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        "address" : "香港北角威非路道11號順興樓地下A號舖",
//        "btype" : 3,
//        "id" : 35,
//        "imgPath" : "Inchcape-Mobility.jpg",
//        "lat" : 114.1904139,
//        "long" : 22.2861514,
//        "openHour" : "星期一至六及公眾假期 :\n上午9:00至晚上6:00\n星期日 : 休息",
//        "phoneNo" : "28801515",
//        "region" : "np"
        prevModel = BranchModel(
            35, "香港北角威非路道11號順興樓地下A號舖", "np",
            22.2861514, 114.1904139,
            "星期一至六及公眾假期 :\n上午9:00至晚上6:00\n星期日 : 休息",
            "Inchcape-Mobility.jpg",
            null, "28801515", 0
        )
//        val isEnglish = SharedPref.getSharedPreferenceBoolean(context, SharedPref.LANGUAGE, true)
//        if (isEnglish) {
//            myRef = database.getReference("branches")
//        } else {
//            myRef = database.getReference("branches_zh")
//        }
        myRef = database.getReference("branches")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                branchList.clear()
                for (snapshot: DataSnapshot in ds.children) {
                    val address: String = snapshot.child("address").getValue(String::class.java)!!
                    val bid = snapshot.child("id").getValue(Int::class.java)!!
                    val long: Double = snapshot.child("long").getValue(Double::class.java)!!
                    val lat: Double = snapshot.child("lat").getValue(Double::class.java)!!
                    val openHour: String = snapshot.child("openHour").getValue(String::class.java)!!
                    val region: String = snapshot.child("region").getValue(String::class.java)!!
                    var slogo: String?
                    var imgPath: String
                    val phoneNo: String = snapshot.child("phoneNo").getValue(String::class.java)!!
                    val dsSlogo: String? = snapshot.child("slogn").getValue(String::class.java)
                    val dsImgPath: String? = snapshot.child("imgPath").getValue(String::class.java)
                    val bType: Int = snapshot.child("btype").getValue(Int::class.java)!!

                    if (dsSlogo != null) {
                        slogo = dsSlogo
                    } else {
                        slogo = null
                    }
                    if (dsImgPath != null) {
                        imgPath = dsImgPath;

                    } else {
                        imgPath = "Inchcape-Mobility.jpg"
                    }

                    val model = BranchModel(
                        bid,
                        address,
                        region,
                        long,
                        lat,
                        openHour,
                        imgPath,
                        slogo,
                        phoneNo,
                        bType
                    )
//                    val model = snapshot.getValue(BranchModel::class.java)
//                    branchList.add(model!!)
                    branchList.add(model)
                }
                mAdapter?.updateList(branchList)
                filteredList = ArrayList(branchList)
//                Log.d("johnson***", "branch number: " + branchList.size.toString())
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Connect is cancelled", Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_branch, container, false)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbarInActivity = activity!!.findViewById(R.id.toolbar)
        recyclerView = view.findViewById(R.id.recycler_view)
//        recyclerView.itemAnimator = SlideInUpAnimator() as RecyclerView.ItemAnimator
        swipeRefreshRecyclerList = view.findViewById(R.id.swipe_refresh_recycler_list)
        fab = view.findViewById(R.id.fab)
        filterButtons = arrayOfNulls<Button>(4)
        filterButtons[0] = view.findViewById(R.id.locAll)
        filterButtons[1] = view.findViewById(R.id.locKowloon)
        filterButtons[2] = view.findViewById(R.id.locHongKong)
        filterButtons[3] = view.findViewById(R.id.locNewTerr)
        btn_unfocus = filterButtons[0]!!
        btn_unfocus.background.setTint(ContextCompat.getColor(activity!!, R.color.pGold))
        for (fb in filterButtons) {
            fb?.setOnClickListener(this)
        }
        fab.setOnClickListener(this)
        spBrand = view.findViewById<NDSpinner>(R.id.spBrand)
        spBrand.onItemSelectedListener = this
        spBranchType = view.findViewById<NDSpinner>(R.id.spBranchType)
        spBranchType.onItemSelectedListener = this

        val brandAdapter: ArrayAdapter<String> =
            ArrayAdapterWithHint<String>(context!!, android.R.layout.simple_spinner_dropdown_item)
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        arrayOf(
            R.string.toyota, R.string.hino, R.string.daihatsu,
            R.string.lexus,
            R.string.spin_all,
            R.string.title_brand_brand
        ).forEach { brandAdapter.add(getString(it)) }
        spBrand.adapter = brandAdapter
        spBrand.setSelection(brandAdapter.count)

        val bTypeAdapter =
            ArrayAdapterWithHint<String>(context!!, android.R.layout.simple_spinner_dropdown_item)
        bTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        arrayOf(
            R.string.brand_type_exh,
            R.string.brand_type_main,
            R.string.brand_type_parts,
            R.string.brand_type_rental,
            R.string.spin_all,
            R.string.title_brand_type
        ).forEach { bTypeAdapter.add(getString(it)) }
        spBranchType.adapter = bTypeAdapter
        spBranchType.setSelection(bTypeAdapter.count)

//        brandAdapter.setDropDownViewResource()


        (activity as AppCompatActivity).setSupportActionBar(toolbarInActivity)
        swipeRefreshRecyclerList.setOnRefreshListener {
            Handler().postDelayed({
                mAdapter?.notifyDataSetChanged()
//                Toast.makeText(context, "冇料到", Toast.LENGTH_SHORT).show()
                if (swipeRefreshRecyclerList.isRefreshing) swipeRefreshRecyclerList.isRefreshing =
                    false
            }, 700)
        }
        setAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.location_search, menu)
        super.onCreateOptionsMenu(menu, inflater)


        val menuItem: MenuItem = menu.findItem(R.id.map_search)
        val searchView: SearchView =
            MenuItemCompat.getActionView(menu.findItem(R.id.location_search)) as SearchView
        val searchManager: SearchManager =
            activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        val searchEdit: EditText =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEdit.setTextColor(Color.WHITE)
        searchEdit.setHintTextColor(Color.WHITE)
        searchEdit.setBackgroundColor(Color.TRANSPARENT)

        val filterArray: Array<InputFilter?> = arrayOfNulls<InputFilter>(2)
        filterArray[0] = InputFilter.LengthFilter(40)

        filterArray[1] =
            InputFilter { source: CharSequence, start: Int, end: Int, _: Spanned, _: Int, _: Int ->
                for (i in start until end) {
                    if (!Character.isLetterOrDigit(source[i])) return@InputFilter ""
                }
                null
            }

        searchEdit.filters = filterArray
        val v: View = searchView.findViewById(androidx.appcompat.R.id.search_plate)
        v.setBackgroundColor(Color.TRANSPARENT)
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                val tempList = ArrayList<BranchModel>()
                if (s.length > 0) {
                    for (i in filteredList.indices) {
                        if (filteredList[i].getAddress().toLowerCase().contains(s.toLowerCase())) {
                            tempList.add(filteredList[i])
                            mAdapter?.updateList(tempList)
                        }
                    }
                } else {
                    mAdapter?.updateList(filteredList)
                }
                return false
            }
        })
    }

    private fun setAdapter() {

        mAdapter = BranchListAdapter(activity, branchList)


        recyclerView.setHasFixedSize(true)

        // use a linear layout manager
//        GridLayoutManager
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                context!!, R.drawable.divider_recyclerview
            )!!
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        recyclerView.adapter = mAdapter

        scrollListener = object : RecyclerViewScrollListener() {
            override fun onEndOfScrollReached(rv: RecyclerView?) {
                Toast.makeText(
                    context,
                    "End of the RecyclerView reached. Do your pagination stuff here",
                    Toast.LENGTH_SHORT
                ).show()
                scrollListener.disableScrollListener()
            }
        }


        mAdapter!!.SetOnItemClickListener(object : BranchListAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int, model: BranchModel) {
//                Toast.makeText(context, model.getAddress(), Toast.LENGTH_SHORT).show()
                prevModel = model

                switchToMap()
            }

            override fun onContactClick(view: View?, position: Int, model: BranchModel?) {
                callFromDailer(context!!, model!!.getPhoneNo()!!)
            }
        })
//                callFromDailer(context!!, model!!.getPhoneNo()!!)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
        if (item.itemId == R.id.map_search) {
            val frag = MapFragment.newInstance(prevModel)

            val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
            ft.replace(R.id.branch_layout, frag, "mapFragment")
                .addToBackStack("mapFragment")
                .commit()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.locAll -> {
                setFocus(btn_unfocus, filterButtons[0]!!)
                regList = null
            }
            R.id.locKowloon -> {
                setFocus(btn_unfocus, filterButtons[1]!!)
                regList = arrayOf("lck", "kb", "kt")
            }
            R.id.locHongKong -> {
                setFocus(btn_unfocus, filterButtons[2]!!)
                regList = arrayOf("np", "wc")
            }
            R.id.locNewTerr -> {
                setFocus(btn_unfocus, filterButtons[3]!!)
                regList = arrayOf("yl", "st", "tw", "kc")
            }
            R.id.fab -> {
                recyclerView.smoothScrollToPosition(0)
                return
            }
        }
        if (regList != null) {
            filteredList.clear()
            for (i in branchList.indices) {
                if (regList!!.contains(branchList[i].getRegion())) {
                    filteredList.add(branchList[i])
                    mAdapter?.updateList(filteredList)
                }
            }
        } else {
            filteredList = ArrayList(branchList)
            mAdapter?.updateList(branchList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setFocus(btn_unfocus: Button, btn_focus: Button) {
        btn_unfocus.background.setTint(
            ContextCompat.getColor(
                activity!!,
                R.color.pColor4_medSlaBlue
            )
        )
        btn_focus.background.setTint(ContextCompat.getColor(activity!!, R.color.pGold))
        // gold color
        this.btn_unfocus = btn_focus
    }

    fun callFromDailer(mContext: Context, number: String) {
        try {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$number")
            mContext.startActivity(callIntent)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Toast.makeText(mContext, "No SIM Found", Toast.LENGTH_LONG).show()
        }
    }

    private fun switchToMap() {
        val frag = MapFragment.newInstance(prevModel)

        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.replace(R.id.branch_layout, frag, "mapFragment")
            .addToBackStack("mapFragment")
            .commit()
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        var slogo: String? = null
        when(parent?.id) {
            R.id.spBrand -> {
                brand_id = position + 1
                when(position) {
                    0 -> {
                        slogo = "slogo_toyota.gif"
                    }
                    1 -> {
                        slogo = "slogo_hino.gif"
                    }
                    2 -> {
                        slogo = "slogo_daihatsu.gif"
                    }
                    3 -> {
                        slogo = "slogo_lexus.gif"
                    }
                    else -> {
                        brand_id = 0
                    }
                }
            }
            R.id.spBranchType -> {
                bType_id = position + 1
                when(position) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                    else -> {
                        bType_id = 0
                    }
                }
            }
        }
        if (brand_id != 0 || bType_id != 0) {
//            filteredList.clear()
            var tempList = ArrayList<BranchModel>()
            if (brand_id != 0) {
                for (i in filteredList.indices) {
//            if (regList != null) {
                    if (slogo.equals(filteredList[i].getSlogn())) {
                        tempList.add(filteredList[i])
                    }
                }
            }
            else {
                tempList = ArrayList(filteredList)
            }
            if (bType_id != 0) {
                val tempList2 = ArrayList<BranchModel>()
                for (i in tempList.indices) {
                    if ((bType_id - 1) == tempList[i].getBType()) {
                        tempList2.add(tempList[i])
                    }
                }
                mAdapter?.updateList(tempList2)
//                if (tempList2.isEmpty()) {
//                }
            }
            else {
                mAdapter?.updateList(tempList)
            }
        }
        else {
            mAdapter?.updateList(filteredList)
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun filterBranch() {
//        if (regList != null) {
//
//        }
    }
}