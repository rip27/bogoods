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
import kotlinx.android.synthetic.main.edit_barang.*
import kotlinx.android.synthetic.main.edit_store.*
import kotlinx.android.synthetic.main.edit_store.image_store_edit
import java.io.IOException

class EditBarang : AppCompatActivity() {

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
        setContentView(R.layout.edit_barang)

        setSupportActionBar(toolbar_edit_barang)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pref = Pref(this)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        fAuth = FirebaseAuth.getInstance()
        val uid = fAuth.currentUser?.uid
        val idstore = intent.getStringExtra("idstore")
        val idbarang = intent.getStringExtra("idbarang")

        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("imagebarang").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Glide.with(this@EditBarang)
                            .load(p0.value.toString())
                            .error(R.drawable.ic_reseller)
                            .into(image_barang_edit)
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("namabarang").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        et_nama_barang_edit.setText(p0.value.toString())
                    }

                }
            )
        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("desc").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        et_desc_barang_edit.setText(p0.value.toString())
                    }

                }
            )
        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("harga").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        et_harga_barang_edit.setText(p0.value.toString())
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("stok").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        et_stok_barang_edit.setText(p0.value.toString())
                    }

                }
            )

        bt_set_image_barang.setOnClickListener {
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
        bt_save_edit_barang.setOnClickListener {
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
                        .centerCrop().into(image_barang_edit)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
        }
    }

    private fun saveData() {
        val uidUser = fAuth.currentUser?.uid
        val idstore = intent.getStringExtra("idstore")
        val idbarang = intent.getStringExtra("idbarang")
        dbRef = FirebaseDatabase.getInstance().reference
        val nb = et_nama_barang_edit.text.toString()
        val hg = et_harga_barang_edit.text.toString()
        val st = et_stok_barang_edit.text.toString()
        val ds = et_desc_barang_edit.text.toString()
        try {
            val storageRef: StorageReference = storageReference
                .child("$uidUser/profile/${pref.getUIDD()}.${GetFileExtension(filePathImage)}")
            storageRef.putFile(filePathImage).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    dbRef.child("store/$idstore/listbarang/$idbarang/imagebarang").setValue(it.toString())
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
        dbRef.child("store/$idstore/listbarang/$idbarang/namabarang").setValue(nb)
        dbRef.child("store/$idstore/listbarang/$idbarang/harga").setValue(hg)
        dbRef.child("store/$idstore/listbarang/$idbarang/stok").setValue(st)
        dbRef.child("store/$idstore/listbarang/$idbarang/desc").setValue(ds)
        Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            onBackPressed()
        }, 1000)
    }

}
