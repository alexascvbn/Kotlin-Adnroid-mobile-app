package com.example.bottonavigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.bottonavigation.database.LocalDatabase
import com.example.bottonavigation.model.OrderParts
import com.example.bottonavigation.model.PartsModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_main.*

class PartsDetailActivity : AppCompatActivity() {
    var part_name: TextView? = null
    var part_price: TextView? = null
    var part_description: TextView? = null
    var part_newOrOld_detail: TextView? = null
    var part_suitableCar_detail: TextView? = null
    var part_image: ImageView? = null

    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    var btnCart: FloatingActionButton? = null
    private lateinit var numberButton: ElegantNumberButton

    var partId = ""
    var currentPart = PartsModel()

    private lateinit var database: FirebaseDatabase
    private lateinit var parts: DatabaseReference

    private var storage_parts_url: String? = null

    private val REQUEST_EXTERNAL = 405


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts_detail)

        database = FirebaseDatabase.getInstance()
        parts = database.getReference("parts")

        numberButton = findViewById(R.id.number_button)
        btnCart = findViewById(R.id.btnCart)

        part_description = findViewById(R.id.part_description_datail)
        part_name = findViewById(R.id.part_name_detail)
        part_price = findViewById(R.id.part_price_datail)
        part_newOrOld_detail = findViewById(R.id.part_newOrOld_datail)
        part_suitableCar_detail = findViewById(R.id.part_suitableCar_datail)
        part_image = findViewById(R.id.part_image_detail)

        collapsingToolbarLayout = findViewById(R.id.collapsing)
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar)
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar)
        storage_parts_url = this.getString(R.string.storage_parts_url)

        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar?.title = "Parts Detail"
        actionbar?.setDisplayHomeAsUpEnabled(true)


        btnCart?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_EXTERNAL)
                }
                else {
                    Log.d("johnson****", "No permission on SD card")
//                    Toast.makeText(baseContext, "No permission on SD card", Toast.LENGTH_SHORT).show()
                }
            }
//            currentPart.image

            val newOrderParts = OrderParts(
                    currentPart.categoryId,
                    currentPart.id,
                    currentPart.name,
                    currentPart.price,
                    numberButton.number.toInt(),
                    currentPart.image,
                    currentPart.description
                )
            val result_code: Long = LocalDatabase(baseContext).addToCart(newOrderParts)
//            LocalDatabase(baseContext).clearAllCart()

            if (result_code > 0L) {
                // , code: ${result_code}
                Toast.makeText(baseContext, "record ${currentPart.id} added , qty : ${numberButton.number}", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(baseContext, "error code : $result_code", Toast.LENGTH_SHORT).show()
            }
        }


        if (intent != null)
            partId = intent.getStringExtra("PartId")
        if (!partId.isEmpty()) {
            getDetailPart(partId)
        }
    }

    private fun getDetailPart(partId: String) {
        parts.child(partId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                currentPart = p0.getValue(PartsModel::class.java) as PartsModel
                Picasso.get().load(storage_parts_url + currentPart.image + "?alt=media")
                    .into(part_image)
                collapsingToolbarLayout.setTitle(currentPart.name)
                part_price?.setText(String.format("%.2f", currentPart.price))
                part_name?.setText(currentPart.name)
                part_description?.setText(currentPart.description)
                part_newOrOld_detail?.setText(currentPart.newOrOld)
                part_suitableCar_detail?.setText(currentPart.suitableCar)
            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

