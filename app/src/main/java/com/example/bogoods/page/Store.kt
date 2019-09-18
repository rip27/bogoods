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
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.adapter.StoreAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.add_store.*
import kotlinx.android.synthetic.main.profile.*
import kotlinx.android.synthetic.main.store.*
import java.io.IOException
import java.util.*

class Store : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    var value = 0.0
    lateinit var filePathImage: Uri
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference
    private var storeAdapter: StoreAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<StoreModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store)
        setSupportActionBar(toolbar_store)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

        val uid = fAuth.currentUser?.uid

        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("job").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val job = p0.value.toString()
                        if (job == "reseller") {
                            add_store.setOnClickListener {
                                Toast.makeText(this@Store, "ANDA BUKAN SELLER", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            tv_warning.visibility = View.VISIBLE

                        } else {
                            add_store.setOnClickListener {
                                showDialogAddStore()
                            }

                            init()


                        }

                    }

                }
            )
    }

    private fun init() {
        var linearLayoutManager = LinearLayoutManager(this@Store)
        recyclerView = findViewById(R.id.rc_store)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)

        fAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance()
            .reference.child("store")
        dbRef.orderByChild("status").equalTo("y")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    list = ArrayList()
                    for (dataSnapshot in p0.children) {
                        val addDataAll = dataSnapshot.getValue(
                            StoreModel::class.java
                        )
                        addDataAll!!.key = dataSnapshot.key
                        if (addDataAll.idpemilik.toString() == fAuth.currentUser?.uid) {
                            list.add(addDataAll)
                        }
                        storeAdapter = StoreAdapter(this@Store, list)
                        recyclerView!!.adapter = storeAdapter
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e(
                        "TAG_ERROR", p0.message
                    )
                }
            })
    }

    private fun showDialogAddStore() {
        var dialog: AlertDialog
        val alertDialog = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.add_store, null)
        alertDialog.setView(view)
        alertDialog.setTitle("DAFTAR")
        alertDialog.setPositiveButton("DAFTAR") { dialog, i ->
            val stname = view.findViewById<EditText>(R.id.et_storename).text.toString()
            val address = view.findViewById<EditText>(R.id.et_address).text.toString()
            if (stname.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Fill All Data", Toast.LENGTH_SHORT).show()
            } else {
                addStore(stname, address)
            }
        }
        alertDialog.setNegativeButton("NO") { dialog, i ->
            dialog.dismiss()
        }
        dialog = alertDialog.create()
        dialog.show()
    }

    private fun addStore(stname: String, address: String) {
        val nameXXX = UUID.randomUUID().toString()
        val idstore = UUID.randomUUID().toString()
        val uid = fAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("store/$idstore")
        dbRef.child("idstore").setValue(idstore)
        dbRef.child("storename").setValue(stname)
        dbRef.child("address").setValue(address)
        dbRef.child("status").setValue("n")
        dbRef.child("idpemilik").setValue(uid)
        Toast.makeText(
            this,
            "Sukses Daftar Toko Mohon Tunggu Konfirmasi dari Admin",
            Toast.LENGTH_SHORT
        ).show()
    }
}
