package com.example.bottonavigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.bottonavigation.bottomFragment.*
import com.example.bottonavigation.sidebarFragment.*

/**
 * Screens available for display in the main screen, with their respective titles,
 * icons, and menu item IDs and fragments.
 */
enum class MainScreen(
    @IdRes val menuItemId: Int,
    @DrawableRes val menuItemIconId: Int,
    @StringRes val titleStringId: Int,
    val fragment: Fragment
) {
    HOME(
        R.id.bottom_navigation_item_home,
        R.drawable.ic_home_black_24dp,
        R.string.activity_main_bottom_screen_home,
        HomeFragment()
    ),
    PARTSCATEGORY(
        R.id.bottom_navigation_item_nav_partsCategory,
        R.drawable.ic_map_black_24dp,
        R.string.activity_main_bottom_screen_partsCategory,
        PartsCategoryFragment()
    ),
    BRANCH(
        R.id.bottom_navigation_item_map,
        R.drawable.ic_map_black_24dp,
        R.string.activity_main_bottom_screen_branch,
        BranchFragment()
    ),
    PROFILE(
        R.id.bottom_navigation_item_nav_profile,
        R.drawable.ic_action_person,
        R.string.activity_main_bottom_screen_profile,
        Profile2Fragment()
    )
}

fun getMainScreenForMenuItem(menuItemId: Int): MainScreen? {
    for (mainScreen in MainScreen.values()) {
        if (mainScreen.menuItemId == menuItemId) {
            return mainScreen
        }
    }
    return null
}

enum class SideButton (
    @IdRes val menuItemId: Int,
    @DrawableRes val menuItemIconId: Int,
    @StringRes val titleStringId: Int,
    val fragment: BaseSideFragment?
) {
    PROFILE(
        R.id.nav_profile,
        R.drawable.ic_person_black_24dp,
        R.string.menu_profile,
        ProfileFragment()
    ),
    ORDER(
        R.id.nav_order,
        R.drawable.ic_featured_play_list_black_24dp,
        R.string.menu_order,
        OrderFragment()
    ),
    CHATBOT(
        R.id.nav_chatbot,
        R.drawable.ic_chat_bubble_black_24dp,
        R.string.menu_chatbot,
        ChatBotFragment()
    ),
    COUPON(
        R.id.nav_coupon,
        R.drawable.ic_coupon_black_24dp,
        R.string.menu_coupon,
        ChatBotFragment()
    ),
    CART(
        R.id.nav_cart,
        R.drawable.ic_shopping_cart_black_24dp,
        R.string.menu_cart,
        CartFragment()
    ),
    SETTINGS(
        R.id.nav_setting,
        R.drawable.ic_settings_black_24dp,
        R.string.menu_settings,
        SettingsFragment()
    ),
    LOGOUT(
        R.id.nav_logout,
        R.drawable.ic_logout_black_24dp,
        R.string.menu_logout,
        null
    )
}