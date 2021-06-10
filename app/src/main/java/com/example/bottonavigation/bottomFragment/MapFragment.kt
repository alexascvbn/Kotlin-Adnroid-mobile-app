package com.example.bottonavigation.bottomFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bottonavigation.R
import com.example.bottonavigation.model.BranchModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


private const val BRANCH_MODEL = "BRANCH_MODEL"

class MapFragment : Fragment(), OnMapReadyCallback, View.OnClickListener {

    var mapFragment: SupportMapFragment? = null
    private lateinit var mMap: GoogleMap
    private lateinit var toolbarInActivity : Toolbar

    private lateinit var sab: ActionBar
    private lateinit var txtAddress: TextView
    private lateinit var imgLocation: ImageView
    private lateinit var btnNav: Button
    private lateinit var cvBranch: CardView

    private var model: BranchModel? = null
    private var address: String? = null
    private var lat: Double? = null
    private var long: Double? = null
    private lateinit var mLoc : Marker

    companion object {
        @JvmStatic
        fun newInstance(branchModel: BranchModel) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BRANCH_MODEL, branchModel)
                }
        }
    }

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            model = it.getSerializable(BRANCH_MODEL) as BranchModel
        }
        long = model?.getLong()
        lat = model?.getLat()
        address = model?.getAddress()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_map, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        if (mapFragment == null) {
            val ft : FragmentTransaction = fragmentManager!!.beginTransaction()
            val mapFragment = SupportMapFragment.newInstance()
            ft.replace(R.id.map, mapFragment, "mapFragment").commit()
        }

        txtAddress = view.findViewById(R.id.txtAddress)
        imgLocation = view.findViewById(R.id.imgLocation)
        btnNav = view.findViewById(R.id.btnNav)
        btnNav.setOnClickListener(this)

        cvBranch = view.findViewById(R.id.cvBranch)
        cvBranch.setOnClickListener(this)

        val storage_url = resources.getString(R.string.storage_url)
//        <string name="storage_url" translatable="false">https://firebasestorage.googleapis.com/v0/b/carsellingapp-e5200.appspot.com/o/%s%s%s?alt=media</string>
        val img_url: String =
            String.format(storage_url, "branch", "%2F", model!!.getImgPath())
        txtAddress.text = address
        Glide.with(context!!)
            .load(img_url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .error(ContextCompat.getDrawable(context!!, R.drawable.inchcape_mobility))
            .into(imgLocation)
        mapFragment!!.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbarInActivity = activity!!.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbarInActivity)

        sab = (activity as (AppCompatActivity)).supportActionBar!!
        sab.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.setOnMarkerClickListener {
            return@setOnMarkerClickListener false
        }

        // Add a marker in Sydney and move the camera
        var location : LatLng
        if (long != null && lat != null) {
            location = LatLng(long!!, lat!!)
            mLoc = mMap.addMarker(MarkerOptions()
                .flat(true)
                .anchor(0.5f, 0.5f)
                .position(location)
                .title(address))
            mLoc.tag = 0
            mLoc.showInfoWindow()
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(location, 16.0f)
            )
        }
        else {
            // loop json and show all model
        }
//        Toast.makeText(context, location.latitude.toString() + " " + location.longitude.toString(), Toast.LENGTH_SHORT).show()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

//        inflater.inflate(R.menu.show_map, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val mapIcon : MenuItem? = menu.findItem(R.id.map_search)
        mapIcon?.setVisible(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.list_icon) {
//            fragmentManager?.popBackStack()
//            Log.d("johnson***", "THis should not be select")
//        }
//        else if (item.itemId == android.R.id.home) {
//            fragmentManager!!.popBackStack()
//            sab.setDisplayHomeAsUpEnabled(false)
//        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        sab.setDisplayHomeAsUpEnabled(false)
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnNav -> {
                val navigation = Uri.parse("google.navigation:q="+address+"&avoid=tf")
                val navigationIntent : Intent = Intent(Intent.ACTION_VIEW, navigation)
                navigationIntent.setPackage("com.google.android.apps.maps")
                startActivity(navigationIntent)
            }
            R.id.cvBranch -> {
                mLoc.showInfoWindow()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(mLoc.position, 16.0f)
                )
            }
        }
    }
}
