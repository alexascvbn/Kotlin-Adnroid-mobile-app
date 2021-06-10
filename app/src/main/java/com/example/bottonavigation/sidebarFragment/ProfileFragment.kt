package com.example.bottonavigation.sidebarFragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.KeyListener
import android.util.Log
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ComplexColorCompat
import com.bumptech.glide.Glide
import com.example.bottonavigation.R
import com.example.bottonavigation.customfonts.MyEditText
import com.example.bottonavigation.customfonts.MyTextView
import com.example.bottonavigation.util.SharedPref
import java.io.File
import kotlin.properties.Delegates


class ProfileFragment : BaseSideFragment() {
    private val userImg by bindView<ImageView>(R.id.userImg)
    private val switcch by bindView<Switch>(R.id.swNotes)

    private val pUsername by bindView<MyTextView>(R.id.username)
    private val pAddress by bindView<MyTextView>(R.id.address)
    private val pEmail by bindView<MyTextView>(R.id.email)
    private val pCompany by bindView<MyEditText>(R.id.company)
//    private val usernameKeyListener = pUsername.keyListener
//    private val addressKeyListener = pAddress.keyListener
//    private val emailKeyListener = pEmail.keyListener
    private lateinit var companyKeyListener : KeyListener

    private lateinit var username: String
    private lateinit var address: String
    private lateinit var email: String
    private lateinit var company: String
    private lateinit var avatar: String
    private var sendNote by Delegates.notNull<Boolean>()
    override val titleId: Int = R.string.menu_profile

    private var isEditing : Boolean = false

    companion object {
        //    internal lateinit var mToolbar : androidx.appcompat.widget.Toolbar
        const val RESULT_LOAD_IMG = 1
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.side_fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        companyKeyListener = pCompany.keyListener

        loadProfile()

        userImg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val imgIntent: Intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(
                    imgIntent,
                    RESULT_LOAD_IMG
                )
            }
        })

        switcch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (switcch.isChecked) {
                    SharedPref.setSharedPreferenceBoolean(context, SharedPref.SENDNOTE, true)
                    Toast.makeText(view.context, "You can receive notification now", Toast.LENGTH_SHORT).show()
                }
                else
                    SharedPref.setSharedPreferenceBoolean(context, SharedPref.SENDNOTE, false)
            }
        })
        pCompany.keyListener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
            val selectedImg: Uri? = data.data
            val defaultImg: Uri =
                Uri.parse("android.resource://com.example.bottonavigation/drawable/ic_person_black_24dp")
            Log.d("johnson***37", selectedImg!!.path)
            try {
                Glide.with(context!!).load(selectedImg).into(userImg)
//                userImg.setImageURI(selectedImg)
                SharedPref.setSharedPreferenceString(
                    context,
                    SharedPref.AVATAR,
                    selectedImg.path
                )
            } catch (ex: Exception) {
                userImg.setImageURI(defaultImg)
                SharedPref.setSharedPreferenceString(
                    context,
                    SharedPref.AVATAR,
                    defaultImg.toString()
                )
            }

        }
    }

    private fun loadProfile() {
        username = SharedPref.getSharedPreferenceString(context, SharedPref.USERNAME, "Anonymus")
        address = SharedPref.getSharedPreferenceString(context, SharedPref.ADDRESS, "Not fill")
        email = SharedPref.getSharedPreferenceString(context, SharedPref.EMAIL, "Not fill")
        company = SharedPref.getSharedPreferenceString(context, SharedPref.COMPANY, "Not fill")
        avatar = SharedPref.getSharedPreferenceString(
            context,
            SharedPref.AVATAR,
            "android.resource://com.example.bottonavigation/drawable/ic_person_black_24dp"
        )
        sendNote = SharedPref.getSharedPreferenceBoolean(context, SharedPref.SENDNOTE, true)

        pUsername.setText(username)
        pEmail.setText(email)
        pCompany.setText(company)
        pAddress.setText(address)
        val avatarFile : File = File(avatar)
        Log.d("johnson***", avatarFile.path)
        if (avatarFile.exists()) {
            val myBitMaps : Bitmap = BitmapFactory.decodeFile(avatarFile.absolutePath)
            userImg.setImageBitmap(myBitMaps)
        }
        else {
            Log.d("johnson***", "cannot find image")
            userImg.setImageURI(Uri.parse("android.resource://com.example.bottonavigation/drawable/ic_person_black_24dp"))
        }

        switcch.isChecked = sendNote
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.editButton) {
            if (!isEditing) {
                item.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_list_white_24dp)
                pCompany.keyListener = companyKeyListener
            }
            else {
                item.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_edit_white_24dp)
                pCompany.keyListener = null
            }
            isEditing = !isEditing
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
            val promptView: View = layoutInflater.inflate(R.layout.profile_prompt, null)
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialog.setView(promptView)
            val etUsername: EditText = promptView.findViewById<EditText>(R.id.etUsername)
            val etEmail: EditText = promptView.findViewById<EditText>(R.id.etEmail)
            val etCompany: EditText = promptView.findViewById<EditText>(R.id.etCompany)
            val etAddress: EditText = promptView.findViewById<EditText>(R.id.etAddress)
            etUsername.setText(username)
            etEmail.setText(email)
            etCompany.setText(company)
            etAddress.setText(address)

            val neutralButton = alertDialog.setCancelable(true)
                .setPositiveButton(getString(R.string.submit), object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        SharedPref.setSharedPreferenceString(
                            context,
                            SharedPref.USERNAME,
                            etUsername.text.toString()
                        )
                        SharedPref.setSharedPreferenceString(
                            context,
                            SharedPref.EMAIL,
                            etEmail.text.toString()
                        )
                        SharedPref.setSharedPreferenceString(
                            context,
                            SharedPref.COMPANY,
                            etCompany.text.toString()
                        )
                        SharedPref.setSharedPreferenceString(
                            context,
                            SharedPref.ADDRESS,
                            etAddress.text.toString()
                        )

                        Toast.makeText(activity, "Data saved.", Toast.LENGTH_SHORT).show()
                        loadProfile()
                    }
                })
                .setNeutralButton(
                    getString(R.string.cancel),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog?.cancel()
                        }
                    })
            alertDialog.create()
            alertDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFileExtension(uri: Uri) : String {
        val cR : ContentResolver = activity!!.contentResolver
        val mime : MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))!!
    }
}

