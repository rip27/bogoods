package com.example.bogoods.page

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.adapter.ListBarangAdapter
import com.example.bogoods.adapter.StoreAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.ListBarangModel
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.add_barang.*
import kotlinx.android.synthetic.main.fill_data.*
import kotlinx.android.synthetic.main.list_barang.*
import kotlinx.android.synthetic.main.store.*
import java.io.IOException
import java.util.*

class ListBarang : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    var value = 0.0
    lateinit var filePathImage: Uri
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<ListBarangModel> = ArrayList()
    private var listBarangAdapter: ListBarangAdapter? = null
    lateinit var img : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_barang)
        setSupportActionBar(toolbar_list_barang)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val idstore = intent.getStringExtra("idstore")
        FirebaseDatabase.getInstance().getReference("store/$idstore/storename")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val names = p0.value.toString()
                    supportActionBar!!.title = "List Barang Toko " + names
                }

            })
        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        val uid = fAuth.currentUser?.uid

        showData()

        add_barang.setOnClickListener {
            showDialogAddBarang()
        }

    }

    private fun showData() {
        var linearLayoutManager = LinearLayoutManager(this@ListBarang)
        recyclerView = findViewById(R.id.rc_barang)
        recyclerView!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView!!.setHasFixedSize(true)

        val idstore = intent.getStringExtra("idstore")
        fAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance()
            .reference.child("store/$idstore/listbarang")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(
                        ListBarangModel::class.java
                    )
                    addDataAll!!.key = dataSnapshot.key
                    list.add(addDataAll)
                    listBarangAdapter = ListBarangAdapter(this@ListBarang, list)
                    recyclerView!!.adapter = listBarangAdapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(
                    "TAG_ERROR", p0.message
                )
            }
        })
    }

    private fun showDialogAddBarang() {
        var dialog: AlertDialog
        val alertDialog = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.add_barang, null)
        alertDialog.setView(view)
        alertDialog.setTitle("TAMBAH BARANG")
        img = view.findViewById<ImageView>(R.id.image_barang)
        img.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) -> {
                    if (ContextCompat.checkSelfPermission(
                            applicationContext,
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
        alertDialog.setPositiveButton("TAMBAH") { dialog, i ->
            val namabarang = view.findViewById<EditText>(R.id.et_nama_barang).text.toString()
            val harga = view.findViewById<EditText>(R.id.et_harga).text.toString()
            val stok = view.findViewById<EditText>(R.id.et_stok).text.toString()
            val desc = view.findViewById<EditText>(R.id.et_desc).text.toString()
            if (namabarang.isEmpty() || harga.isEmpty() || stok.isEmpty()|| desc.isEmpty()) {
                Toast.makeText(this, "Fill All Data", Toast.LENGTH_SHORT).show()
            } else {
                addBarang(namabarang, harga, stok, desc)
            }
        }
        alertDialog.setNegativeButton("NO") { dialog, i ->
            dialog.dismiss()
        }
        dialog = alertDialog.create()
        dialog.show()
    }

    private fun addBarang(namabarang: String, harga: String, stok: String, desc: String) {
        val nameXXX = UUID.randomUUID().toString()
        val idbarang = UUID.randomUUID().toString()
        val idstore = intent.getStringExtra("idstore")
        val uid = fAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
        try {
            val storageRef: StorageReference = storageReference
                .child("$uid/barang/${pref.getUIDD()}.${GetFileExtension(filePathImage)}")
            storageRef.putFile(filePathImage).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    dbRef.child("imagebarang").setValue(it.toString())
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
        dbRef.child("idbarang").setValue(idbarang)
        dbRef.child("idstore").setValue(idstore)
        dbRef.child("namabarang").setValue(namabarang)
        dbRef.child("stok").setValue(stok)
        dbRef.child("desc").setValue(desc)
        dbRef.child("harga").setValue(harga)
        dbRef.child("idpemilik").setValue(uid)
        Toast.makeText(
            this,
            "Sukses Menambah Barang",
            Toast.LENGTH_SHORT
        ).show()
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
                        .centerCrop().into(img)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
        }
    }
}
