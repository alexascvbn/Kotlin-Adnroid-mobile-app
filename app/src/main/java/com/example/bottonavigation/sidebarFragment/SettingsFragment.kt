package com.example.bottonavigation.sidebarFragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.os.ConfigurationCompat
import com.example.bottonavigation.MainActivity
import com.example.bottonavigation.R
import com.example.bottonavigation.util.SharedPref
import com.example.bottonavigation.util.Util
import com.google.firebase.auth.FirebaseAuth
import java.util.*


private const val USERNAME = "username"

// A fragment that might not use
class SettingsFragment : BaseSideFragment(), CompoundButton.OnCheckedChangeListener {
    // TODO: Rename and change types of parameters
    private var myName: String? = null
    private var listener: OnFragmentInteractionListener? = null
    override val titleId: Int = R.string.menu_settings
    private val rdEnglish by bindView<RadioButton>(R.id.rd_language_en)
    private val rdChinese by bindView<RadioButton>(R.id.rd_language_zh)
    private val rdNavRight by bindView<RadioButton>(R.id.rd_nav_right)
    private val rdNavLeft by bindView<RadioButton>(R.id.rd_nav_left)

    private lateinit var auth: FirebaseAuth

    companion object {
        /**
         * @param myName Parameter 1.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.side_fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val current_language = ConfigurationCompat.getLocales(resources.configuration)[0].language
        Toast.makeText(context, current_language, Toast.LENGTH_LONG).show()
        if (current_language == "en") {
            rdEnglish.isChecked = true
        }
        else if (current_language == "zh") {
            rdChinese.isChecked = true
        }
        rdChinese.setOnCheckedChangeListener(this)
        rdEnglish.setOnCheckedChangeListener(this)

        if (SharedPref.getSharedPreferenceBoolean(context, SharedPref.NAV_IS_RIGHT, true)) {
            rdNavRight.isChecked = true
        }
        else {
            rdNavLeft.isChecked = true
        }

        rdNavRight.setOnCheckedChangeListener(this)
        rdNavLeft.setOnCheckedChangeListener(this)
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView?.id) {
            R.id.rd_language_en -> {
                if (isChecked) {
//                    (activity as MainActivity).updateLocale(Locale.ENGLISH)
                    (activity as MainActivity).updateLocale(Locale("en"))
                    Util.refreshActionBarMenu(activity as MainActivity)

                }
            }
            R.id.rd_language_zh -> {
                if (isChecked) {
//                    (activity as MainActivity).updateLocale(Locale.CHINESE)
                    (activity as MainActivity).updateLocale(Locale("zh"))
                    Util.refreshActionBarMenu(activity as MainActivity)
                }
            }

            R.id.rd_nav_right -> {
                if (isChecked) {
                    SharedPref.setSharedPreferenceBoolean(context, SharedPref.NAV_IS_RIGHT, true)
                }
            }
            R.id.rd_nav_left -> {
                if (isChecked) {
                    SharedPref.setSharedPreferenceBoolean(context, SharedPref.NAV_IS_RIGHT, false)
                }
            }
        }
    }
//
//    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
//        when(group?.id) {
//            R.id.lan_config_group -> {
//                if (checkedId == 0) {
//                    (activity as MainActivity).updateLocale(Locale.ENGLISH)
//                }
//                else if (checkedId == 1) {
//                    (activity as MainActivity).updateLocale(Locale.CHINESE)
//                }
//            }
//        }
//    }

}
