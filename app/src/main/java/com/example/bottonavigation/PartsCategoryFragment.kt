package com.example.bottonavigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.bottonavigation.holder.CategoryHolder
import com.example.bottonavigation.listener.ItemClickListener
import com.example.bottonavigation.model.CategoryModel
import com.example.bottonavigation.sidebarFragment.CartFragment
import com.example.bottonavigation.sidebarFragment.OrderFragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class PartsCategoryFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var category: DatabaseReference
    var recycler_category: RecyclerView? = null
    //    var layoutManager: RecyclerView.LayoutManager? = null
//    var categoryModelList: ArrayList<CategoryModel>? = null
    private lateinit var adapter: FirebaseRecyclerAdapter<CategoryModel, CategoryHolder>
    private var gridLayoutManager: GridLayoutManager? = null
    private var storage_category_url: String? = null
    private var fab: FloatingActionButton? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseDatabase.getInstance()
        category = database.reference.child("category")


        val view: View = inflater.inflate(R.layout.activity_parts_category, container, false)
        fab = view.findViewById(R.id.fab_category)
        fab?.setOnClickListener {
            val sideFrag = CartFragment()
            val tag = "cartFragment"
            val ft : FragmentTransaction = fragmentManager!!.beginTransaction()
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            ft.replace(R.id.drawer_layout, sideFrag, tag)
                .addToBackStack(tag)
                .commit()
        }

        recycler_category = view.findViewById(R.id.recycler_category)
        storage_category_url = context?.getString(R.string.storage_category_url)

        gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

        recycler_category.run {
            this?.setHasFixedSize(true)
            this?.layoutManager = gridLayoutManager
            this?.adapter = loadCategory();
        }

        return view
    }

    private fun loadCategory(): FirebaseRecyclerAdapter<CategoryModel, CategoryHolder> {

        val query = FirebaseDatabase.getInstance()
            .reference.child("category")

        val options = FirebaseRecyclerOptions.Builder<CategoryModel>()
            .setQuery(query, CategoryModel::class.java)
//            .setLifecycleOwner(this)
            .build()


        adapter = object : FirebaseRecyclerAdapter<CategoryModel, CategoryHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
                return CategoryHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.category_item, parent, false)
                )
            }

            override fun onBindViewHolder(
                holder: CategoryHolder,
                position: Int,
                model: CategoryModel
            ) {
                holder.txtCategoryName.text = model.name

                context?.let {
                    Glide.with(it)
                        .load(storage_category_url + model.image + "?alt=media")
                        .into(holder.imageCategory)
                }

                val clickItem: CategoryModel? = null

                holder.setItemClickListener(itemClickListener = object : ItemClickListener {
                    override fun onItemClick(v: View, pos: Int) {
                        //Get GategoryId and send to new Activity
                        val partsList = Intent(activity, PartsListActivity::class.java)
                        partsList.putExtra("CategoryId", adapter.getItem(pos).id)

                        startActivity(partsList)
                    }
                })
            }
        }
        return adapter;

    }

    override fun onStart() {
        adapter.startListening();
        super.onStart()
    }

    override fun onStop() {
        adapter.stopListening();
        super.onStop()
    }
}
