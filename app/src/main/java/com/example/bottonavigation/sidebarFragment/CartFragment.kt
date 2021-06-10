package com.example.bottonavigation.sidebarFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.os.ConfigurationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottonavigation.R
import com.example.bottonavigation.adapter.OrderPartListAdapter
import com.example.bottonavigation.database.LocalDatabase
import com.example.bottonavigation.model.OrderModel
import com.example.bottonavigation.model.OrderParts
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet


private const val DELIVERY_ADDRESS = "DELIVERY_ADDRESS"


class CartFragment : BaseSideFragment(), View.OnClickListener {
    private var deliveryAddress: String? = null
    private var credits: Int = 0
    //    private val my_toolbar by bindView<Toolbar>(R.id.my_toolbar)
//    private var listener: OrderFragment.OnFragmentInteractionListener? = null
    private val sideToolbar by bindView<Toolbar>(R.id.sideToolbar)
    private val recyclerView by bindView<RecyclerView>(R.id.recycler_view)
    private val tvTotalPrice by bindView<TextView>(R.id.total_price)
    private val btnPay by bindView<Button>(R.id.btnPay)
    private val cbSelectAll by bindView<CheckBox>(R.id.cbSelectAll)
    private val progressBar by bindView<ProgressBar>(R.id.progressBar)
    private lateinit var menuItem: Menu
    private var showEditGroup: Boolean = false
    private val categoryMap : HashMap<Int, String> = HashMap<Int, String>()

    private lateinit var mOrderPartAdapter: OrderPartListAdapter
    override val titleId: Int = R.string.menu_cart
    private var total_price: Double = 0.00

    private var partList: ArrayList<OrderParts> = ArrayList()
    private val checkSet = HashSet<Int>()
    private var user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var localDataBase: LocalDatabase

//    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.side_fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnPay.setOnClickListener(this)
        cbSelectAll.setOnClickListener(this)
        setAdapter()
    }

    private fun setAdapter() {
        val category: DatabaseReference = database.getReference("category")
        category.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                categoryMap.clear()
                val current_language = ConfigurationCompat.getLocales(resources.configuration)[0].language
                var loc_name = "name_en"
                if (current_language == "en") {
                    loc_name = "name_en"
                }
                else if (current_language == "zh") {
                    loc_name = "name"
                }
                else {
                    loc_name = "name_en"
                }

                for (snapshot: DataSnapshot in ds.children) {
                    val catId: Int? = snapshot.child("id").getValue(Int::class.java)
                    val cat_name: String? = snapshot.child(loc_name).getValue(String::class.java)
                    if (catId == null || cat_name == null) {
                        continue
                    }
                    else {
                        categoryMap.set(catId, cat_name)
//                        Log.d("johnson***", "$catId $cat_name")
                    }
                }
                partList.forEach {
                    it.categoryName = categoryMap.get(it.categoryId)
                }
                mOrderPartAdapter.updateList(partList)
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Connect is cancelled", Toast.LENGTH_SHORT).show()
            }
        })
        localDataBase = LocalDatabase(requireContext())
        partList = localDataBase.getCarts()

        recyclerView.setHasFixedSize(true)
        mOrderPartAdapter = OrderPartListAdapter(
            activity,
            partList
        )
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mOrderPartAdapter
        mOrderPartAdapter.setOnItemClickListener(object : OrderPartListAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, model: OrderParts?) {
//                Toast.makeText(context, model?.name, Toast.LENGTH_SHORT).show()
//                mOrderPartAdapter.
                val checkBox = view?.findViewById<CheckBox>(R.id.check_list)
                checkBox?.let {
                    it.isChecked = !it.isChecked
                }
            }
            override fun onSubBtnClick(
                view: View?,
                isChecked: Boolean,
                position: Int,
                model: OrderParts?
            ) {
                var qty = model?.qty?.minus(1)
                if (qty != null) {
                    if (qty > 0) {
                        model?.qty = qty
                        (view as TextView).text = qty.toString()
                        if (isChecked) {
                            calTotal()
                        }
                    }
                }
            }

            override fun onAddBtnClick(
                view: View?,
                isChecked: Boolean,
                position: Int,
                model: OrderParts?
            ) {
                var qty = model?.qty?.plus(1)
                if (qty != null) {
                    if (qty > 0) {
                        model?.qty = qty
                        (view as TextView).text = qty.toString()
                        if (isChecked) {
                            calTotal()
                        }
                    }
                }
            }
        })
        mOrderPartAdapter.setOnCheckedListener(object : OrderPartListAdapter.OnCheckedListener {
            override fun onChecked(
                view: View?,
                isChecked: Boolean,
                position: Int,
                model: OrderParts?
            ) {

//                Toast.makeText(context, (if (isChecked) "Checked " else "Unchecked ") + model?.name, Toast.LENGTH_SHORT).show()
                if (isChecked) {
                    checkSet.add(position)
                    showEditGroup = true
                    if (checkSet.size == partList.size) {
                        cbSelectAll.isChecked = true
                    }
                }
                else {
                    cbSelectAll.isChecked = false
                    checkSet.remove(position)
                    if (checkSet.isEmpty()) {
                        showEditGroup = false
                    }
                }
                calTotal()

                if (showEditGroup) {
                    showMenuItem()
                }
                else {
                    hideMenuItem()
                }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun calTotal(): Double {
        var total = 0.0
        for (i: Int in checkSet) {
            val part = partList.get(i)
            total += part.qty * part.price
        }
        tvTotalPrice.text = String.format(getString(R.string.price), total)
        this.total_price = total
        return total
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPay -> {
                val builder = AlertDialog.Builder(context)
                if (checkSet.isEmpty()) {
                    builder.run {
                        setTitle("Failed")
                        setMessage("You may select somthing")
                        setPositiveButton(R.string.OK) { dialog, which -> dialog?.cancel() }
                    }
                    builder.create().show()
                    return
                }
                user?.let { curr_user ->
//                    builder.run {
//                        setTitle(R.string.confirmation)
//                        setMessage("Total price : $ $total_price\n Continue?")
//                        setPositiveButton(R.string.CONTINUE) { dialog, which ->
//                            progressBar.visibility = View.VISIBLE
//                            addOrder(curr_user)
//                        }
//                        setNeutralButton(R.string.cancel) { dialog, _ -> dialog.cancel()}
//                    }
                    database.getReference("Users/" + curr_user.uid).addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
//                                TODO("Not yet implemented")
                        }
                        override fun onDataChange(ds: DataSnapshot) {
                            ds.child("deliveryAddress").getValue(String::class.java)?.let {addr ->
                                deliveryAddress = addr
                            }
                            ds.child("credits").getValue(Int::class.java)?.let {cred ->
                                credits = cred
                            }
                        }

                    })
                    database.getReference("max_order_id").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            progressBar.visibility = View.GONE

                            val maxId: Int? = dataSnapshot.getValue(Int::class.java)

                            maxId?.let { max_order_id ->
                                deliveryAddress?.let { mAddress ->
                                    var childUpdates = HashMap<String, Any>()
                                    val order = OrderModel(
                                        max_order_id.toString(),
                                        curr_user.displayName!!,
                                        Date(),
                                        (total_price - credits),
                                        0,
                                        mAddress,
                                        curr_user.uid,
                                        credits
                                    )

                                    val tempList = ArrayList<OrderParts>()
                                    for (i: Int in checkSet) {
                                        tempList.add(partList[i])
                                    }
                                    localDataBase.delCart(tempList)
                                    tempList.forEach{
                                        it.orderId = order.orderId
                                        order.addProduct(it)
                                        partList.remove(it)
                                    }
                                    mOrderPartAdapter.updateList(partList)
                                    cbSelectAll.isChecked = false
                                    var dir =
                                        "/orders/" + curr_user.uid + "/userOrder/" + order.orderId
                                    childUpdates[dir] = order.toMap()
                                    database.reference.updateChildren(childUpdates)
                                        .addOnFailureListener{
                                            Snackbar.make(recyclerView, "Fail to add record", Snackbar.LENGTH_SHORT)
                                                .setAction("Action", null).show()
                                        }

                                    childUpdates = HashMap<String, Any>()
                                    dir = "/max_order_id/"
                                    childUpdates[dir] = max_order_id + 1
                                    database.reference.updateChildren(childUpdates)

                                    builder.run {
                                        setTitle("Ordered")
                                        setPositiveButton(R.string.OK) { dialog, _ -> dialog?.cancel() }
                                    }
                                    if (credits == 0) {
                                        builder.run {
                                            setMessage("order is created:\ntotal price : $ ${order.totalPrice}")
                                        }
                                    }
                                    else {
                                        builder.run {
                                            setMessage("order is created:\ntotal price : $ ${order.totalPrice}" +
                                                    "\nCredit Used : $credits")
                                        }
                                    }

                                    credits = (order.totalPrice / 100).toInt()
                                    childUpdates = HashMap<String, Any>()
                                    dir = "Users/" + curr_user.uid + "/credits"
                                    childUpdates[dir] = credits
                                    database.reference.updateChildren(childUpdates)

                                    builder.create().show()
                                } ?: run {
                                    builder.run {
                                        setTitle("Please set your delivery addresss first")
                                        setPositiveButton(R.string.OK) { dialog, _ -> dialog?.cancel() }
                                    }
                                }
                            } ?: run {
                                builder.run {
                                    setTitle("Can't find max id.")
                                    setPositiveButton(R.string.OK) { dialog, _ -> dialog?.cancel() }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            progressBar.visibility = View.GONE
                            Log.w("firebase", "Failed to read value.", error.toException())
                        }
                    })
                } ?: run {
                    builder.run {
                        setTitle("Failed")
                        setMessage(getString(R.string.should_login_first))
                        setPositiveButton(R.string.OK) { dialog, which -> dialog?.cancel() }
                    }
                    builder.create().show()
                }


            }
            R.id.cbSelectAll ->
                if (cbSelectAll.isChecked) {
                    mOrderPartAdapter.selectAll()
                }
                else {
                    mOrderPartAdapter.unSelectAll()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cart_menu, menu)
        menuItem = menu
        hideMenuItem()
    }
    fun showMenuItem() {
        menuItem.setGroupVisible(R.id.editPartsGroup, true)
    }
    fun hideMenuItem() {
        menuItem.setGroupVisible(R.id.editPartsGroup, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.deleteButton -> {
                val tempList = ArrayList<OrderParts>()
                for (i: Int in checkSet) {
                    tempList.add(partList[i])
                }

                localDataBase.delCart(tempList)
                checkSet.clear()
                tempList.forEach { partList.remove(it) }
                mOrderPartAdapter.updateList(partList)
                cbSelectAll.isChecked = false

                Snackbar.make(recyclerView, "deleted", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
                hideMenuItem()
                calTotal()
            }
            R.id.saveButton -> {
                val tempList = ArrayList<OrderParts>()
                for (i: Int in checkSet) {
                    tempList.add(partList[i])
                }
                localDataBase.updateCart(tempList)
                Snackbar.make(recyclerView, "saved selected item", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addOrder(curr_user: FirebaseUser) {
        val builder = AlertDialog.Builder(context)

    }
}
