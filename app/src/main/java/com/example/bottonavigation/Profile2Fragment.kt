package com.example.bottonavigation

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.Sampler
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.bottonavigation.sidebarFragment.BaseSideFragment
import com.example.bottonavigation.sidebarFragment.OrderFragment
import com.example.bottonavigation.ui.login.LoginActivity2
import com.google.android.gms.tasks.Task
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_prompt.*
import java.time.Instant
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.HashMap


class Profile2Fragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var mLogoIV: ImageView
//    private var mCoverIV: ImageView? = null
    private lateinit var mNameProflie: TextView
    private lateinit var mEmailProflie: TextView
    private lateinit var mPhoneProflie: TextView
    private lateinit var mDeliveryAddress: TextView
    private lateinit var mCreditDesc: TextView
    private lateinit var baseLinearLayout: LinearLayout
    private lateinit var mCreditButton: MaterialCardView
    private lateinit var mGoOrder: MaterialCardView

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fab: FloatingActionButton
    private lateinit var storageReference: StorageReference
    val storagePath: String = "Users_Profile_Cover_Imgs/"


    private lateinit var pd: ProgressDialog
    private val CAMERA_REQUEST_CODE: Int = 100
    private val STORAGE_REQUEST_CODE: Int = 200
    private val IMAGE_PICK_GALLERY_CODE: Int = 300
    private val IMAGE_PICK_CAMERA_CODE: Int = 400

    var cameraPermissions:Array<String> = arrayOf(String())
    var storagePermissions:Array<String>  = arrayOf(String())

    private lateinit var image_uri: Uri
    private lateinit var profileOrCoverPhoto: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.layout_profile_new, container, false)

        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Users")
        storageReference = FirebaseStorage.getInstance().reference

        mLogoIV = view.findViewById(R.id.logoIV)
//        mCoverIV = view.findViewById(R.id.coverIv)
        mNameProflie = view.findViewById(R.id.name_profile)
        mEmailProflie = view.findViewById(R.id.email_profile)
        mPhoneProflie = view.findViewById(R.id.phone_profile)
        mDeliveryAddress = view.findViewById(R.id.address_profile)
        mCreditDesc = view.findViewById(R.id.credit_desc)

        baseLinearLayout = view.findViewById(R.id.base_linearLayout)
        mCreditButton = view.findViewById(R.id.credits_button)
        mGoOrder = view.findViewById(R.id.btnGoOrder)
        fab = view.findViewById(R.id.fab)
        pd = ProgressDialog(getActivity())

        cameraPermissions = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(WRITE_EXTERNAL_STORAGE)


        val query: Query = databaseReference.orderByChild("email").equalTo(user?.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (ds: DataSnapshot in p0.children) {

                    var name: String = "no name"
                    var email: String = "no email"
                    var phone: String = "no phone"
                    var image: String = "no image"
                    var cover: String = "no cover"
                    var deliveryAddress: String = "no delivery address"
                    var credits: Int = 0

                    ds.child("name").value?.let { name = it as String }
                    ds.child("email").value?.let { email = it as String }
                    ds.child("phone").value?.let { phone = it as String }
                    ds.child("image").value?.let { image = it as String }
                    ds.child("cover").value?.let { cover = it as String }
                    ds.child("deliveryAddress").value?.let { deliveryAddress = it as String }
                    ds.child("credits").getValue(Int::class.java)?.let { credits = it }

                    mNameProflie.setText(name)
                    mEmailProflie.setText(email)
                    mPhoneProflie.setText(phone)
                    mDeliveryAddress.setText(deliveryAddress)
                    mCreditDesc.setText(credits.toString())

                    try {

                        Glide.with(view)
                            .load(image)
                            .error(R.drawable.ic_add_image)
                            .into(mLogoIV);
                    } catch (e: Exception) {
                        Picasso.get().load(R.drawable.ic_add_image).into(mLogoIV)
                    }

//                    try {
//                        Picasso.get().load(cover).into(mCoverIV)
//                    } catch (e: Exception) {
//                        Picasso.get().load(R.drawable.ic_add_image).into(mCoverIV)
//                    }
                }
            }
        })

        fab.setOnClickListener {
            showEditProfileDialog()
        }
        mGoOrder.setOnClickListener{
            goSideFragment()
        }
        mCreditButton.setOnClickListener{
            Snackbar.make(baseLinearLayout, "Credit is e-dollar that use as cash,\n" +
                    "with 100 : 1 rebate rate.", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }
        return view
    }

    private fun checkStoragePermission(): Boolean {
        //check if storage permission is enable or not
        //return true is enabled
        //return false if not enalbed
        val result: Boolean = ContextCompat.checkSelfPermission(
            activity!!,
            WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)
        return result
    }

    private fun requestCameraPermission() {
        //request runtime storage permission
        ActivityCompat.requestPermissions(activity!!,storagePermissions, STORAGE_REQUEST_CODE)
    }

    private fun checkCameraPermission(): Boolean {
        val result: Boolean = ContextCompat.checkSelfPermission(
            activity!!,
            CAMERA
        ) == (PackageManager.PERMISSION_GRANTED)
        val result1: Boolean = ContextCompat.checkSelfPermission(
            activity!!,
            WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)
        return result && result1
    }

    private fun requestStoragePermission() {
        //request runtime storage permission
        ActivityCompat.requestPermissions(activity!!,cameraPermissions, CAMERA_REQUEST_CODE)
    }

    private fun showEditProfileDialog() {
        val options = arrayOf("Edit Profile image", "Edit Name", "Edit Phone", "Edit Delivery Address","Update password")
        val builder = AlertDialog.Builder(activity!!)

        builder.run {

            setTitle("Choose Action")
            setItems(options, DialogInterface.OnClickListener { dialog, which ->
                when(which) {
                    0 -> {
                        //Edit profile clicked
                        pd.setMessage("Updating Profile Picture")
                        profileOrCoverPhoto = "image"
                        showImagePicDialot()
                    }
                    1 -> {
                        //Edit Name clicked
                        pd.setMessage("Updating Name")
                        showNamePhoneAddressUpdateDialog("name")
                    }
                    2 -> {
                        //Edit Phone clicked
                        pd.setMessage("Updating Phone")
                        showNamePhoneAddressUpdateDialog("phone")
                    }

                    3 -> {
                        pd.setMessage("Updating Address")
                        showNamePhoneAddressUpdateDialog("deliveryAddress")
                    }

                    4 ->{
                        pd.setMessage("Changing Password")
                        ShowChangePasswordDialog()
                    }

                }
            })
        }
        builder.create().show()

    }

    private fun ShowChangePasswordDialog() {

        val view= LayoutInflater.from(activity).inflate(R.layout.dialog_update_password,null)
        val oldPasswordEt:EditText = view.findViewById(R.id.ud_PasswordEt)
//        Log.d("passwordEt",passwordEt.toString())
        val newPasswordEt:EditText = view.findViewById(R.id.ud_newPasswordEt)
        val updatePasswordBtn:Button = view.findViewById(R.id.updatePasswordBtn)

        val builder = AlertDialog.Builder(context!!)
//        val builder = android.app.AlertDialog.Builder(activity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()

        updatePasswordBtn.setOnClickListener {
            val oldPassword:String = oldPasswordEt.text.toString().trim()
            val newPassword:String = newPasswordEt.text.toString().trim()
            if(TextUtils.isEmpty(oldPassword)){
                Toast.makeText(activity,"Enter your current password...",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(newPassword.length<6){
                Toast.makeText(activity,"Password length must at least 6 characters...",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dialog.dismiss()
            updatePassword(oldPassword,newPassword)
        }
    }

    private fun updatePassword(oldPassword: String, newPassword: String) {
        pd.show()

        //get current user
        val user = auth.currentUser

        //before changing password re-authenticate the user
        val authCredential:AuthCredential = EmailAuthProvider.getCredential(user!!.email.toString(),oldPassword)
        user.reauthenticate(authCredential)
            .addOnSuccessListener {
                //successfully authenticated, begin update
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        // password updated
                        pd.dismiss()
                        Toast.makeText(activity,"Password Updated...",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        //failed updating password, show reason
                        pd.dismiss()
                        Toast.makeText(activity,""+it.message,Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                //authentication failed, show reason
                pd.dismiss()
                Toast.makeText(activity,""+it.message,Toast.LENGTH_SHORT).show()
            }
    }

    private fun showNamePhoneAddressUpdateDialog(key: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Update $key")

        val linearLayout = LinearLayout(activity!!)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(10, 10, 10, 10)

        val editText = EditText(activity!!)
        editText.setHint("Enter $key")
        linearLayout.addView(editText)

        builder.setView(linearLayout)

        builder.setPositiveButton("Update") { dialog, which ->
            val value = editText.text.toString().trim()
            if (!TextUtils.isEmpty(value)) {
                val result: HashMap<String, String> = HashMap()
                result.put(key, value)

                user?.let {user ->
                    databaseReference.child(user.uid).updateChildren(result as Map<String, Any>)
                        .addOnSuccessListener {
                            pd.dismiss()
                            Toast.makeText(context, "Updated... ", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            pd.dismiss()
                            Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
                        }
                } ?: run {
                    Toast.makeText(context, getString(R.string.should_login_first), Toast.LENGTH_SHORT).show()
                }
            }
        }.setNegativeButton("Cancel") { dialog, which ->
            pd.dismiss()
        }
        builder.create().show()

    }

    private fun showImagePicDialot() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(activity!!)

        builder.run {

            setTitle("Pick Image Form")
            setItems(options, DialogInterface.OnClickListener { dialog, which ->
                if (which == 0) {
                    //Camera clicked

                    if (!checkCameraPermission()) {
                        requestCameraPermission()
                    } else {
                        pickFromCamera()
                    }

                } else if (which == 1) {
                    //Gallery clicked
                    if (!checkStoragePermission()) {
                        requestStoragePermission()
                    } else {
                        pickFromGallery()
                    }
                }
            })
        }
        builder.create().show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            //picking from camera, first check if camera and storage permissions allowed or not
            CAMERA_REQUEST_CODE ->
                if (grantResults.size > 0) {
                    val cameraAccepted: Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val writeStorageAccepted: Boolean = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && writeStorageAccepted) {
                        //permission enabled
                        pickFromCamera()
                    } else {
                        Toast.makeText(
                            context,
                            "Please enable camera & storage permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            STORAGE_REQUEST_CODE ->
                if (grantResults.isNotEmpty()) {
                    Toast.makeText(context, grantResults.size.toString(), Toast.LENGTH_SHORT).show()
                    val writeStorageAccepted: Boolean = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (writeStorageAccepted) {
                        //permission enabled
                        pickFromGallery()
                    } else {
                        Toast.makeText(
                            context,
                            "Please enable storage permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun pickFromCamera() {
        //Intent of picking image from device camera
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Desciption")
        //put image uri
        image_uri = activity?.contentResolver!!.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //intent to start camera
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE)

    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.setType("image/*")
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //This method will be called after picking image from Camera or Gallery.
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //iamge is picked from gallery, get uri of image
                image_uri = data!!.data
                uploadProfileCoverPhoto(image_uri)
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //iamge is picked from gallery, get uri of image
                uploadProfileCoverPhoto(image_uri)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadProfileCoverPhoto(Uri: Uri) {
        pd.show()
        val filePathAndName: String = storagePath + "" + profileOrCoverPhoto + "_" + user?.uid

        val storageReference2nd: StorageReference = storageReference.child(filePathAndName)
        storageReference2nd.putFile(Uri)
            .addOnSuccessListener {
                val uriTask: Task<Uri> = it.storage.downloadUrl
                Log.d("aaaaaa",it.storage.downloadUrl.toString())

                while (!uriTask.isSuccessful) {};
                val downloadUri = uriTask.result
                if (uriTask.isSuccessful) {
                    val results: HashMap<String, String> = HashMap()
                    results.put(profileOrCoverPhoto, downloadUri.toString())

                    databaseReference.child(user!!.uid)
                        .updateChildren(results as Map<String, Any>)
                        .addOnSuccessListener {
                            pd.dismiss()
                            Toast.makeText(activity!!, "Image Updated", Toast.LENGTH_SHORT)
                                .show()
                        }.addOnFailureListener {
                            pd.dismiss()
                            Toast.makeText(
                                activity!!,
                                "Error Updating image...",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                } else {
                    pd.dismiss()
                    Toast.makeText(activity!!, "Some error occured", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                pd.dismiss()
                Toast.makeText(activity!!, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        if (auth.currentUser != null) {
//            inflater.inflate(R.menu.profile2_menu, menu)
//        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_logout) {
            auth.signOut()
            startActivity(Intent(activity, MainActivity::class.java))
        }
        return false
    }

    private fun goSideFragment() {
        val sideFrag = OrderFragment.newInstance("default")
        val tag = "orderFragment"
        val ft : FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        ft.replace(R.id.drawer_layout, sideFrag, tag)
            .addToBackStack(tag)
            .commit()
//        (activity as MainActivity).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
}
