package com.example.bottonavigation.sidebarFragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottonavigation.R
import com.example.bottonavigation.adapter.OrderDetailAdapter
import com.example.bottonavigation.model.BaseParts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.side_fragment_profile.*

private const val ORDER_ID = "ORDER_ID"

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
class OrderDetailFragment : BaseSideFragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    override val titleId: Int = R.string.menu_order
    private var mOrderId : String? = null

    private val tvOrderId by bindView<TextView>(R.id.tvOrderId)
    private val tvDeliveryAddress by bindView<TextView>(R.id.tvDeliveryAddress)
    private val tvOrderDate by bindView<TextView>(R.id.tvOrderDate)
    private val tvTotalPrice by bindView<TextView>(R.id.tvTotalPrice)
    private val linearLayout by bindView<LinearLayout>(R.id.linearLayout)
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    private val recyclerView by bindView<RecyclerView>(R.id.recycler_view)
    private lateinit var mOrderDetailAdapter: OrderDetailAdapter
    private var bPartsList: ArrayList<BaseParts> = ArrayList()

    companion object {
        @JvmStatic
        fun newInstance(orderId: String) =
            OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ORDER_ID, orderId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mOrderId = it.getString(ORDER_ID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.side_fragment_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    fun setAdapter() {
        recyclerView.setHasFixedSize(true)

        mOrderDetailAdapter = OrderDetailAdapter(
            activity as Context,
            bPartsList
        )
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mOrderDetailAdapter
        mOrderDetailAdapter.setOnItemClickListener(object : OrderDetailAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, model: BaseParts) {
                Toast.makeText(context!!, position.toString(), Toast.LENGTH_SHORT).show()
//                val builder: AlertDialog.Builder = AlertDialog.Builder(activity as Context)
//                val linearLayout = LinearLayout(activity)
//                linearLayout.orientation = LinearLayout.VERTICAL
//                linearLayout.setPadding(10, 10, 10, 10)
//                val partsdata = TextView(activity)
//                val parts_str = "Parts ID : ${model.id}\n" +
//                        "Name : ${model.name}\n" +
//                        "Category ID : ${model.categoryId}\n" +
//                        "Price : ${model.price}\n" +
//                        "Quantity: ${model.qty}"
//                val parts_str = String.format(getString(R.string.parts_str),
//                    model.id,
//                    model.name,
//                    model.categoryId,
//                    model.price,
//                    model.qty)
//                partsdata.setText(parts_str)
//                linearLayout.addView(partsdata)
//                builder.setView(linearLayout)
//                builder.run {
//                    setPositiveButton("")
//                }
//                builder.create().show()
            }
        })

        auth.currentUser?.let { user ->
            mOrderId?.let { myId ->
                val myRef = database.getReference("orders/"+user.uid+"/userOrder/"+myId+"/")
                myRef.addValueEventListener(object : ValueEventListener{

                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onDataChange(ds: DataSnapshot) {
                        val oid = ds.key
                        val deliveryAddress = ds.child("deliveryAddress").getValue(String::class.java)
                        val orderDate = ds.child("orderDate").getValue(String::class.java)
                        val status = ds.child("status").getValue(Int::class.java)
                        val totalPrice = ds.child("totalPrice").getValue(Double::class.java)
                        if (myId == oid) {
                            tvOrderId.text =  String.format(resources.getString(R.string.order_orderId), oid)
                            tvDeliveryAddress.text = String.format(resources.getString(R.string.order_orderAddress),
                                deliveryAddress)
                            tvOrderDate.text = String.format(resources.getString(R.string.order_orderDate),
                                orderDate)
                            tvTotalPrice.text = String.format(resources.getString(R.string.order_totalPrice), totalPrice)
                        }
//                        val cxt = context!!
//                        when(status) {
//                            0 -> {
//                                linearLayout.background.setTint(ContextCompat.getColor(cxt, android.R.color.holo_blue_bright))
//                            }
//                            1 -> {
//                                linearLayout.background.setTint(ContextCompat.getColor(cxt, android.R.color.holo_blue_dark))
//                            }
//                            2 -> {
//                                linearLayout.background.setTint(ContextCompat.getColor(cxt, android.R.color.holo_green_dark))
//                            }
//                            3 -> {
//                                linearLayout.background.setTint(ContextCompat.getColor(cxt, android.R.color.holo_red_light))
//                            }
//                            4 -> {
//                                linearLayout.background.setTint(ContextCompat.getColor(cxt, android.R.color.darker_gray))
//                            }
//                        }
                        bPartsList.clear()
                        for (part: DataSnapshot in ds.child("partsList").children) {
                            var id: String = ""
                            var name: String = ""
                            var categoryId: Int = 0
                            var qty: Int = 0
                            var price: Double = 0.0
                            part.child("id").value?.let { id = it as String }
                            part.child("name").value?.let { name = it as String }
                            part.child("categoryId").value?.let { categoryId = (it as Long).toInt() }
                            part.child("qty").value?.let { qty = (it as Long).toInt() }
                            part.child("price").value?.let {
                                if (it is Long) {
                                    price = (it as Long).toDouble()
                                }
                                else {
                                    price = it as Double
                                }
                            }
                            bPartsList.add(
                                BaseParts(id,
                                    name,
                                    categoryId,
                                    price,
                                    qty)
                            )
                        }
                        mOrderDetailAdapter.updateList(bPartsList)
                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }

                })
            } ?: run {
                Toast.makeText(context, getString(R.string.order_not_found), Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, getString(R.string.should_login_first), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.editButton -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(activity as Context)
                builder.run {
                    setMessage("Canncel Order?")
                    setPositiveButton(R.string.confirm) {_, _ ->

                        auth.currentUser?.let { user ->
                            mOrderId?.let { myId ->
                                var childUpdates = HashMap<String, Any>()
                                var dir = "orders/" + user.uid + "/userOrder/" + myId + "/status"
                                childUpdates[dir] = 3  // status id 3 is cancelled
                                database.reference.updateChildren(childUpdates)
                                    .addOnSuccessListener {
                                        val builder2: AlertDialog.Builder = AlertDialog.Builder(activity as Context)
                                        builder2.run {
                                            setTitle(R.string.success)
                                            setPositiveButton(R.string.OK) {dialog, _ ->
                                                dialog.cancel()
                                            }
                                        }
                                        builder2.create().show()
                                    }
                            }
                        }
                    }
                    setNegativeButton(R.string.cancel) {dialog, _ ->
                        dialog.cancel()
                    }
                }
                builder.create().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {

    }

}
