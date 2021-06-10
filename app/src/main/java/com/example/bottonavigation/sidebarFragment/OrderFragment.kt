package com.example.bottonavigation.sidebarFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.bottonavigation.R
import com.example.bottonavigation.adapter.OrderExpandableListAdapter
import com.example.bottonavigation.model.OrderModel
import com.example.bottonavigation.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USERNAME = "username"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OrderFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : BaseSideFragment() {
    // TODO: Rename and change types of parameters
    private var myName: String? = null
    override val titleId: Int = R.string.menu_order
    private val elvOrder by bindView<ExpandableListView>(R.id.elvOrder)
    private lateinit var listAdapter: OrderExpandableListAdapter
    private val orderList: ArrayList<OrderModel> = ArrayList()
    private val database = FirebaseDatabase.getInstance()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        @JvmStatic
        fun newInstance(myName: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, myName)
                }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myName = it.getString(USERNAME)
        }

        auth.currentUser?.let {user ->
            database.getReference("orders/"+user.uid+"/userOrder/").addValueEventListener(object : ValueEventListener{

                override fun onDataChange(dss: DataSnapshot) {
                    orderList.clear()
                    dss.children.forEach{
                        val oid = it.key!!
                        val orderDate = it.child("orderDate").getValue(String::class.java)!!
                        val status = it.child("status").getValue(Int::class.java)!!
                        val totalPrice = it.child("totalPrice").getValue(Double::class.java)!!
                        val deliveryAddress = it.child("deliveryAddress").getValue(String::class.java)!!
                        val model = OrderModel(
                            oid,
                            user.email.toString(),
                            Util.dateFormat.parse(orderDate),
                            totalPrice,
                            status,
                            deliveryAddress,
                            user.uid,
                            0
                        )
                        orderList.add(model)
                    }
                    if (orderList.isEmpty()) {
                        Toast.makeText(context, "you have not buy anything", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        listAdapter.updateList(orderList)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                }

            })
        } ?: run {
//            for (i in 1..4){
//            val orderDemoDetail1 = OrderModel("012", "johnson", dateFormat.parse("28/12/2019"), 300.0, i, "abc's home")
//            orderList.add(orderDemoDetail1)
//            }
            Toast.makeText(context, getString(R.string.should_login_first), Toast.LENGTH_SHORT).show()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.side_fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        public OrderModel(String orderId, String user, Date orderDate, double totalPrice) {
//        public OrderPartModel(String id, String name, String type, Double price, int qty) {
        listAdapter = OrderExpandableListAdapter(context!!)
        listAdapter.updateList(orderList)
        elvOrder.setAdapter(listAdapter)
        elvOrder.setOnGroupClickListener(object : ExpandableListView.OnGroupClickListener{
            override fun onGroupClick(
                parent: ExpandableListView?,
                v: View?,
                pos: Int,
                id: Long
            ): Boolean {
                val ft : FragmentTransaction? = fragmentManager?.beginTransaction()
                val frag = OrderDetailFragment.newInstance(orderList[pos].orderId)
                ft?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                ft?.add(R.id.drawer_layout, frag, "orderDetailFragment")
                    ?.addToBackStack("orderDetailFragment")
                    ?.commit()
//                Toast.makeText(context, orderList[pos].orderId, Toast.LENGTH_SHORT).show()
//                orderList[pos]
                return true
            }
        })

        elvOrder.setOnChildClickListener(object : ExpandableListView.OnChildClickListener{
            override fun onChildClick(
                parent: ExpandableListView?,
                v: View?,
                groupPosition: Int,
                childPosition: Int,
                id: Long
            ): Boolean {
                Toast.makeText(context, "this function is useless...\nhard to make UI", Toast.LENGTH_SHORT).show()
                return true
            }
        })
    }


}
