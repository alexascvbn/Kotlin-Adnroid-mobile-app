package com.example.bottonavigation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.bottonavigation.holder.PartsHolder
import com.example.bottonavigation.listener.ItemClickListener
import com.example.bottonavigation.model.PartsModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.paging.LoadingState
import com.google.firebase.database.*
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter
import kotlinx.android.synthetic.main.app_bar_main.toolbar


class PartsListActivity : AppCompatActivity() {
    private lateinit var partList: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var recycler_view: RecyclerView? = null
    var categoryId: Int = 0;
//    private lateinit var tvNoParts: TextView
    private lateinit var adapter: FirebaseRecyclerAdapter<PartsModel, PartsHolder>
    private var storage_parats_url: String? = null
    private var isLinear: Boolean = true
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
//    private var layoutManage : GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts_list)

        recycler_view?.bringToFront()
        recycler_view = findViewById(R.id.recycler_partsView)
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        database = FirebaseDatabase.getInstance()
//        layoutManage = GridLayoutManager(this,1)
//        recycler_view?.layoutManager = layoutManage
//        partList = database.reference.child("parts")

//        Log.d("recycler_view", recycler_view.toString())
        storage_parats_url = this.getString(R.string.storage_parts_url)

        recycler_view?.run {
            this.setHasFixedSize(true)
//            this.layoutManager = LinearLayoutManager(context)
            this.layoutManager = GridLayoutManager(context, 1)
        }

        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar?.title = "Parts"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        if (intent != null)
            categoryId = intent.getIntExtra("CategoryId", 0)
        if (categoryId > 0) {
            loadListPart(categoryId)
        }
//        adapter.let {
//            if (it.itemCount == 0) {
//                Toast.makeText(this, it.itemCount.toString(), Toast.LENGTH_SHORT).show()
//                tvNoParts.visibility = View.VISIBLE
//            }
//        }
    }

    private fun loadListPart(categoryId: Int) {

        val pageConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(5)
            .setPageSize(10)
            .build()


//        Log.d("categoryId", categoryId.toString());
        val query = database
//            .reference.child("parts")
//            .reference.child("parts").equalTo("categoryId", categoryId.toDouble())
            .reference.child("parts").orderByChild("categoryId").equalTo(categoryId.toDouble())
//            .reference.child("parts").orderByChild("categoryId")
//            .reference.child("parts").equalTo(categoryId.toDouble(), "categoryId")
//            .reference.child("parts").equalTo(categoryId.toDouble())

//        Log.d("category_id",database.reference.orderByChild("categoryId").ref.key)

        val options = FirebaseRecyclerOptions.Builder<PartsModel>()
            .setQuery(query, PartsModel::class.java)
            .setLifecycleOwner(this)
            .build()
//        val pageOptions = DatabasePagingOptions.Builder<PartsModel>()
//            .setLifecycleOwner(this@PartsListActivity)
//            .setQuery(query, pageConfig, PartsModel::class.java)
//            .build()
//        pageOptions.run {
//        }

        adapter = object : FirebaseRecyclerAdapter<PartsModel, PartsHolder>(options) {
//        adapter = object : FirebaseRecyclerPagingAdapter<PartsModel, PartsHolder>(pageOptions) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartsHolder {

                return PartsHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.parts_item, parent, false)
                )
            }

            override fun onBindViewHolder(holder: PartsHolder, position: Int, model: PartsModel) {
                holder.bind(model)

                Glide.with(applicationContext)
                    .load(storage_parats_url + model.image + "?alt=media")
                    .into(holder.part_image)
//                Picasso.get().load(storage_parats_url + model.image + "?alt=media")
//                    .into(holder.part_image)

                Log.d("image_url", storage_parats_url + model.image + "?alt=media")

                val animation: Animation =
                    AnimationUtils.loadAnimation(baseContext, android.R.anim.slide_in_left)
                holder.itemView.startAnimation(animation)

                holder.setItemClickListener(object : ItemClickListener {
                    override fun onItemClick(v: View, pos: Int) {
                        val partsDatail = Intent(baseContext, PartsDetailActivity::class.java)
                        partsDatail.putExtra("PartId", adapter?.getRef(pos)?.key)
//                        Log.d("key", adapter?.getRef(pos).key)
                        startActivity(partsDatail)
                    }

                })
            }
        }
        mSwipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                adapter.notifyDataSetChanged()
                if (mSwipeRefreshLayout.isRefreshing) mSwipeRefreshLayout.isRefreshing =
                    false
            }, 700)
        }
        recycler_view?.adapter = adapter
//        Log.d("adapter", adapter.toString())
        recycler_view?.adapter?.let {
            if (it.itemCount == 0) {
//                tvNoParts.visibility = View.VISIBLE
            }
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.switch_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.itemId
        recycler_view?.run {
            if (id == R.id.switch_view) {
                if (isLinear) {
                    this.layoutManager = GridLayoutManager(context, 2)
                    item.setIcon(R.drawable.ic_list_white_24dp)
                }
                else {
                    this.layoutManager = GridLayoutManager(context, 1)
                    item.setIcon(R.drawable.ic_grid_white_24dp)
                }
                isLinear = !isLinear
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        adapter?.startListening()
        super.onStart()
    }

    //
    override fun onStop() {
        adapter?.stopListening()
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
