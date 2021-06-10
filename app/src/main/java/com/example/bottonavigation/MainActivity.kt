package com.example.bottonavigation

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.example.bottonavigation.adapter.MainPagerAdapter
import com.example.bottonavigation.sidebarFragment.*
import com.example.bottonavigation.util.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.properties.Delegates


class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var mainPagerAdapter: MainPagerAdapter

    private val viewPager by bindView<ViewPager>(R.id.view_pager)
    private val bottomNavigationView by bindView<BottomNavigationView>(R.id.bottom_navigation_view)
    private val toolbar  by bindView<Toolbar>(R.id.toolbar)
    private val titleText  by bindView<TextView>(R.id.titleText)
    private val navView by bindView<NavigationView>(R.id.nav_view)
    private var current_mainpage_pos : Int = 0
    private var menuView: Menu? = null
    public val drawerLayout by bindView<DrawerLayout>(R.id.drawer_layout)

    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var mFirebaseAuthLis : FirebaseAuth.AuthStateListener
    var user : FirebaseUser? = null
    var mDeliveryAddress : String = "Not Fill"
    var nav_is_right by Delegates.notNull<Boolean>()

//    private val isShowingMap : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        // Check auth on Activity start
        mFirebaseAuthLis = FirebaseAuth.AuthStateListener {
            user = it.currentUser
            user?.let {
                database.getReference("users/"+it.uid+"/").addValueEventListener(object :
                    ValueEventListener {

                    override fun onDataChange(p0: DataSnapshot) {
                        val myAddress = p0.child("deliveryAddress").getValue(String::class.java)
                        if (myAddress == null)
                            mDeliveryAddress = "Not Fill"
                        else
                            mDeliveryAddress = myAddress
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, "Event is cancelled", Toast.LENGTH_SHORT).show()
                    }
                })
            } ?: run {
//                loginUI(null)
            }
        }


        // set nav controller
//        findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
//        bottom_navigation_view
//        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

        // Initialize components/views.
        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)
        setSupportActionBar(toolbar)


//        run {
//            this.sideMenu = menu
//        }
        nav_is_right = SharedPref.getSharedPreferenceBoolean(this, SharedPref.NAV_IS_RIGHT, true)
//
        val params = DrawerLayout.LayoutParams(
            DrawerLayout.LayoutParams.WRAP_CONTENT,
            DrawerLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            width = DrawerLayout.LayoutParams.WRAP_CONTENT
            height = DrawerLayout.LayoutParams.MATCH_PARENT
            gravity = if (nav_is_right) {
                Gravity.END
            } else {
                Gravity.START
            }
        }
        navView.layoutParams = params

        navView.setOnCreateContextMenuListener { menu, v, menuInfo ->
//            super.(menu, v, menuInfo)
            menuView = menu

        }
        navView.setNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.nav_profile -> {
                    goSideFragment(ProfileFragment(), "profileFragment")
                }
                R.id.nav_order -> {
                    goSideFragment(OrderFragment.newInstance("default"), "orderFragment")
                }
                R.id.nav_chatbot -> {
//                        goSideFragment(ChatBotFragment.newInstance("johnson"), "chatBotFragment")
                    goSideFragment(ChatBotFragment(), "chatBotFragment")
                }
                R.id.nav_coupon -> {
                    goSideFragment(CouponFragment(), "couponFragment")
                }
                R.id.nav_cart -> {
                    goSideFragment(CartFragment(), "cartFragment")
                }
                R.id.nav_setting -> {
                    goSideFragment(SettingsFragment(), "settingsFragment")
                }
                R.id.nav_logout -> {
                    auth.signOut()
                    Toast.makeText(applicationContext, "You have logged out", Toast.LENGTH_LONG).show()
                }
                else -> return@setNavigationItemSelectedListener false
            }
            true
        }


        // Set items to be displayed.
        mainPagerAdapter.setItems(
            arrayListOf(
                MainScreen.HOME,
                MainScreen.PARTSCATEGORY,
                MainScreen.BRANCH,
                MainScreen.PROFILE
            )
        )

        // Show the default screen.
        val defaultScreen = MainScreen.HOME
        scrollToScreen(defaultScreen)
        selectBottomNavigationViewMenuItem(defaultScreen.menuItemId)
        supportActionBar?.setTitle(defaultScreen.titleStringId)
        titleText.text = getString(defaultScreen.titleStringId)

        // Set the listener for item selection in the bottom navigation view.
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        // Attach an adapter to the view pager and make it select the bottom navigation
        // menu item and change the title to proper values when selected.
        viewPager.adapter = mainPagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                current_mainpage_pos = position
                val selectedScreen = mainPagerAdapter.getItems()[position]
                selectBottomNavigationViewMenuItem(selectedScreen.menuItemId)
//                supportActionBar?.setTitle(selectedScreen.titleStringId)
                titleText.text = getString(selectedScreen.titleStringId)
            }
        })
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        // enable sidebar
        if (count == 0) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            if (drawerLayout.isDrawerOpen(navView)) {
//            drawerLayout.isFocusableInTouchMode = false
                drawerLayout.closeDrawer(navView)
            }
            else {
                super.onBackPressed()
            }
        } else {
            supportFragmentManager.popBackStack()
        }

    }

    /**
     * Scrolls ViewPager to show the provided screen.
     */
    private fun scrollToScreen(mainScreen: MainScreen) {
        val screenPosition = mainPagerAdapter.getItems().indexOf(mainScreen)
        if (screenPosition != viewPager.currentItem) {
            viewPager.currentItem = screenPosition
        }
    }

    /**
     * Selects the specified item in the bottom navigation view.
     */
    private fun selectBottomNavigationViewMenuItem(@IdRes menuItemId: Int) {
        bottomNavigationView.setOnNavigationItemSelectedListener(null)
        bottomNavigationView.selectedItemId = menuItemId
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        getMainScreenForMenuItem(menuItem.itemId)?.let {
            scrollToScreen(it)
//            supportActionBar?.setTitle(it.titleStringId)
            titleText.text = getString(it.titleStringId)

            return true

        }
//        if (menuItem.itemId != MainScreen.BRANCH.menuItemId) {
//            MainActivity@this.supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mMenuInflater : MenuInflater = menuInflater
        mMenuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.toolbar_setting -> {
                if (nav_is_right)
                    drawerLayout.openDrawer(GravityCompat.END)
                else
                    drawerLayout.openDrawer(GravityCompat.START)
            }
        }
//        val bundle = Bundle()
//        bundle.putString("edttext", "From Activity")
// set Fragmentclass Arguments
//        MainScreen.HOME.fragment.setArguments(bundle)
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        return true
//        return super.onSupportNavigateUp()
    }

    public fun <T : View> Activity.bindView(@IdRes res: Int): Lazy<T> {
        return lazy { findViewById<T>(res) }
    }

    private fun goSideFragment(sideFrag : BaseSideFragment, tag : String) {
        val ft : FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        ft.replace(R.id.drawer_layout, sideFrag, tag)
            .addToBackStack(tag)
            .commit()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }


    override fun updateLocale(locale: Locale) {
        super.updateLocale(locale)
//        val selectedScreen = mainPagerAdapter.getItems()[current_mainpage_pos]
//        selectBottomNavigationViewMenuItem(selectedScreen.menuItemId)
//                supportActionBar?.setTitle(selectedScreen.titleStringId)
//        titleText.text = getString(selectedScreen.titleStringId)
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(mFirebaseAuthLis)
//        loginUI(null)
    }

    private fun loginUI(currentUser : FirebaseUser?) {
//        val frag: Fragment = mainPagerAdapter.getItems()[0].fragment
        menuView?.let {
            Toast.makeText(applicationContext, it.size().toString(), Toast.LENGTH_SHORT)
                .show()
        }
        if (currentUser != null) {
            menuView?.findItem(R.id.nav_coupon)?.isVisible = true
            menuView?.findItem(R.id.nav_profile)?.isVisible = true

            menuView?.findItem(R.id.nav_logout)?.isVisible = false
        }
        else {
            menuView?.findItem(R.id.nav_coupon)?.isVisible = false
            menuView?.findItem(R.id.nav_profile)?.isVisible = false

            menuView?.findItem(R.id.nav_logout)?.isVisible = true
        }
    }

}
