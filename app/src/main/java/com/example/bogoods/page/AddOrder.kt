package com.example.bogoods.page

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bogoods.R
import com.example.bogoods.adapter.ListBarangAdapter
import com.example.bogoods.adapter.StoreAddOrderAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.ListBarangModel
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.add_order.*
import java.util.ArrayList

class AddOrder : AppCompatActivity() {

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
    private var list: MutableList<StoreModel> = ArrayList()
    private var adapter: StoreAddOrderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_order)
        setSupportActionBar(toolbar_add_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        showData()


    }
    private fun showData() {
        var linearLayoutManager = LinearLayoutManager(this@AddOrder)
        recyclerView = findViewById(R.id.rc_cstore)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)

        val idstore = intent.getStringExtra("idstore")
        fAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance()
            .reference.child("store/")
        dbRef.orderByChild("status").equalTo("y").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(
                        StoreModel::class.java
                    )
                    addDataAll!!.key = dataSnapshot.key
                    FirebaseDatabase.getInstance()
                        .reference.child("store/${dataSnapshot.key}")
                        .child("requestconnection").orderByChild("status").equalTo("accept")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()){
                                    list.add(addDataAll)
                                }
                            }

                        })
                    adapter = StoreAddOrderAdapter(this@AddOrder, list)
                    recyclerView!!.adapter = adapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(
                    "TAG_ERROR", p0.message
                )
            }
        })
    }
}
