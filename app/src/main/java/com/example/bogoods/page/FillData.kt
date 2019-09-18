package com.example.bogoods.page

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.fill_data.*
import java.io.IOException

class FillData : AppCompatActivity() {

    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref
    lateinit var dbRef: DatabaseReference
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    var value = 0.0
    lateinit var filePathImage: Uri
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fill_data)

        setSupportActionBar(toolbar_filldata)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pref = Pref(this)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        fAuth = FirebaseAuth.getInstance()
        val job = intent.getStringExtra("job")

        if (job == "seller"){
            Glide.with(this@FillData).load(R.drawable.ic_seller)
                .into(photo_profile_fd)
        }else{
            Glide.with(this@FillData).load(R.drawable.ic_reseller)
                .into(photo_profile_fd)
        }

        set_pp.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), PERMISSION_RC
                        )
                    } else {
                        imageChooser()
                    }
                }
                else -> {
                    imageChooser()
                }
            }
        }

        bt_save.setOnClickListener {
            save()
        }

    }


    private fun imageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
            REQUEST_CODE_IMAGE
        )
    }
    fun GetFileExtension(uri: Uri): String? {
        val contentResolver = this.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                filePathImage = data?.data!!
                try {
                    val bitmap: Bitmap = MediaStore
                        .Images.Media.getBitmap(
                        this.contentResolver, filePathImage
                    )
                    Glide.with(this).load(bitmap)
                        .centerCrop().into(photo_profile_fd)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
        }
    }


    private fun save() {
        val uid = fAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().reference
        val eteditnamao = et_name_fd.text.toString()
        val eteditphoneo = et_phone_fd.text.toString()
        val email = fAuth.currentUser?.email
        val jb = intent.getStringExtra("job")
        val speditgender = sp_gender_fd.selectedItem.toString()
        try {
            val storageRef: StorageReference = storageReference
                .child("$uid/profile/${pref.getUIDD()}.${GetFileExtension(filePathImage)}")
            storageRef.putFile(filePathImage).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    dbRef.child("user/$uid/profile").setValue(it.toString())
                }
            }.addOnFailureListener {
                Log.e("TAG_ERROR", it.message)
            }.addOnProgressListener { taskSnapshot ->
                value = (100.0 * taskSnapshot
                    .bytesTransferred / taskSnapshot.totalByteCount)
            }
        } catch (e: UninitializedPropertyAccessException) {
            Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show()
        }
        dbRef.child("user/$uid/name").setValue(eteditnamao)
        dbRef.child("user/$uid/phone").setValue(eteditphoneo)
        dbRef.child("user/$uid/gender").setValue(speditgender)
        dbRef.child("user/$uid/email").setValue(email)
        dbRef.child("user/$uid/id").setValue(uid)
        dbRef.child("user/$uid/job").setValue(jb)
        Toast.makeText(this, "Data Terisi", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            val intent = Intent(this@FillData, Dashboard::class.java)
            startActivity(intent)
        }, 1000)
    }


}
