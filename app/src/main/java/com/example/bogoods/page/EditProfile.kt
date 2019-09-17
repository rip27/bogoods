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
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.edit_profile.*
import java.io.IOException

class EditProfile : AppCompatActivity() {

    lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var pref: Pref
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    var value = 0.0
    lateinit var filePathImage: Uri
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)
        setSupportActionBar(toolbarEditSeller)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pref = Pref(this)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        fAuth = FirebaseAuth.getInstance()
        val uid = fAuth.currentUser?.uid

        foto_profile_seller_edit.setOnClickListener {
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

        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("profile").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Glide.with(this@EditProfile)
                            .load(p0.value.toString())
                            .error(R.drawable.ic_reseller)
                            .into(foto_profile_seller_edit)
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("name").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        et_name_seller_edit.setText(p0.value.toString())
                    }

                }
            )
        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("phone").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        et_phone_seller_edit.setText(p0.value.toString())
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("gender").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val gend = p0.value.toString()
                        if (gend == "Laki-Laki"){
                            sp_gender_seller_edit.setSelection(0)
                        }else{
                            sp_gender_seller_edit.setSelection(1)
                        }
                    }

                }
            )
        bt_save_profile_seller.setOnClickListener {
            saveData()
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
                        .centerCrop().into(foto_profile_seller_edit)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
        }
    }


    private fun saveData() {
        val uidUser = fAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().reference
        val eteditnamao = et_name_seller_edit.text.toString()
        val eteditphoneo = et_phone_seller_edit.text.toString()
        val speditgender = sp_gender_seller_edit.selectedItem.toString()
        try {
            val storageRef: StorageReference = storageReference
                .child("$uidUser/profile/${pref.getUIDD()}.${GetFileExtension(filePathImage)}")
            storageRef.putFile(filePathImage).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    dbRef.child("user/$uidUser/profile").setValue(it.toString())
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
        dbRef.child("user/$uidUser/name").setValue(eteditnamao)
        dbRef.child("user/$uidUser/phone").setValue(eteditphoneo)
        dbRef.child("user/$uidUser/gender").setValue(speditgender)
        Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            onBackPressed()
        }, 1000)
    }

}
