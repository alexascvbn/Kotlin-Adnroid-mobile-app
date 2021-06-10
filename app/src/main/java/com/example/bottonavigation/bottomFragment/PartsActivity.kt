package com.example.bottonavigation.bottomFragment

import android.os.Bundle
import android.os.PersistableBundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottonavigation.R
import com.example.bottonavigation.adapter.PartsAdapter
import com.example.bottonavigation.model.PartsModel

class PartsActivity : AppCompatActivity() {

    var mRecyclerView: RecyclerView? = null
    var partsAdapter: PartsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        //set its properties
        mRecyclerView?.layoutManager = LinearLayoutManager(this) // LinearLayout
//        mRecyclerView?.layoutManager = GridLayoutManager(this, 2)  // GridLayout, here 2 means 2 columns
        mRecyclerView?.setHasFixedSize(true);

        mRecyclerView?.findViewById<RecyclerView>(R.id.recycler_partsView)

        //adapter
        partsAdapter = PartsAdapter(this, getPlayers())
        mRecyclerView?.adapter = partsAdapter
        setContentView(R.layout.layout_viewproducts)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        //recyclerview
//        val rootView = inflater.inflate(R.layout.layout_viewproducts, container, false)
//        mRecyclerView = rootView?.findViewById(R.id.recycler_partsView)
//
//
////        setHasOptionsMenu(true);
//
//        return rootView
//    }

    //add models to arraylist
    private fun getPlayers(): ArrayList<PartsModel> {
        val models = ArrayList<PartsModel>()

//        val partsInfos = arrayOf(
//            arrayOf(
//                "a35875",
//                "催化 / 中至尾",
//                "REMUS INNOVATION M3 M4 中至尾死氣喉",
//                "全奧地利製造、靚聲、高質素 Fit 位 .TUV最高品質認證 Porsche、Benz AMG、McLaren等原廠排氣喉製造商",
//                R.drawable.parts_1
//            ),
//            arrayOf(
//                "a35875",
//                "催化 / 中至尾",
//                "REMUS INNOVATION M3 M4 中至尾死氣喉",
//                "全奧地利製造、靚聲、高質素 Fit 位 .TUV最高品質認證 Porsche、Benz AMG、McLaren等原廠排氣喉製造商",
//                R.drawable.parts_1
//            ),
//            arrayOf(
//                "a35875",
//                "催化 / 中至尾",
//                "REMUS INNOVATION M3 M4 中至尾死氣喉",
//                "全奧地利製造、靚聲、高質素 Fit 位 .TUV最高品質認證 Porsche、Benz AMG、McLaren等原廠排氣喉製造商",
//                R.drawable.parts_1
//            ),
//            arrayOf(
//                "a35875",
//                "催化 / 中至尾",
//                "REMUS INNOVATION M3 M4 中至尾死氣喉",
//                "全奧地利製造、靚聲、高質素 Fit 位 .TUV最高品質認證 Porsche、Benz AMG、McLaren等原廠排氣喉製造商",
//                R.drawable.parts_1
//            ),
//            arrayOf(
//                "a35875",
//                "催化 / 中至尾",
//                "REMUS INNOVATION M3 M4 中至尾死氣喉",
//                "全奧地利製造、靚聲、高質素 Fit 位 .TUV最高品質認證 Porsche、Benz AMG、McLaren等原廠排氣喉製造商",
//                R.drawable.parts_1
//            )
//        )
//        for (value in partsInfos) {
//            val p = PartsModel(
//                value[0].toString(),
//                value[1].toString(),
//                value[2].toString(),
//                value[3].toString(),
//                value[4] as Int
//            )
//
//            models.add(p);
//        }


//        val p1 = PartsModel(
//            "a35875",
//            "催化 / 中至尾",
//            "REMUS INNOVATION M3 M4 中至尾死氣喉",
//            "全奧地利製造、靚聲、高質素 Fit 位 .TUV最高品質認證 Porsche、Benz AMG、McLaren等原廠排氣喉製造商",
//            R.drawable.parts_1
//        )
//        models.add(p1)
//
//        val p2 = PartsModel(
//            "a36858",
//            "避震套裝",
//            "RS-R BEST I 高性能避震機 STREAM RN6 RN8 RSZ HONDA",
//            "RS-R 高性能避震機、36段軟硬調教、高低調教、金屬塔頂、反應快、全日本製造、香港行貨有保養大量安裝案例",
//            R.drawable.parts_2
//        )
//        models.add(p2)
//
//        val p3 = PartsModel(
//            "a34124",
//            "頭尾泵把",
//            "Audi A7改 RS7 大包圍(前期改尾期)4G可散賣 自取再折",
//            "PP膠完裝物料 XF 工廠 有電子條碼 包括頭泵把、中網、(中網可選顏色 後擾流及尾咀",
//            R.drawable.parts_3
//        )
//        models.add(p3)
//
//        val p4 = PartsModel(
//            "a42466",
//            "制動系統類",
//            "AP RACING 95系列鍛造剎車套餐 (前6後4) 30系專用",
//            "AP RACING 9560 鍛造 6/4POT、AP RACING370-390mm 碟、PROJECT MU皮、本公司設有信用卡分期服務",
//            R.drawable.parts_4
//        )
//        models.add(p4)


        return models
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.parts_menu, menu)
//        // Inflate the menu to use in the action bar
//        val item: MenuItem = menu!!.findItem(R.id.action_search)
//        val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                //called when you click search
//                partsAdapter?.getFilter()?.filter(newText)
//                return false
//            }
//
//            override fun onQueryTextSubmit(query: String): Boolean {
//                // called whenever you type each letter in searchview
//                partsAdapter?.getFilter()?.filter(query)
//                return false
//            }
//
//        })
//
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.parts_menu, menu)
//        // Inflate the menu to use in the action bar
//        val item: MenuItem = menu.findItem(R.id.action_search)
//        val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                //called when you click search
//                partsAdapter?.getFilter()?.filter(newText)
//                return false
//            }
//
//            override fun onQueryTextSubmit(query: String): Boolean {
//                // called whenever you type each letter in searchview
//                partsAdapter?.getFilter()?.filter(query)
//                return false
//            }
//
//        })
//
//        return super.onCreateOptionsMenu(menu, inflater)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.action_setting) {
//            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

}