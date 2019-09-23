package com.example.bogoods.page

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bogoods.R
import com.example.bogoods.adapter.ConStoreAdapter
import com.example.bogoods.adapter.StoreAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.con_store.*
import java.util.ArrayList

class ReqConStore : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref
    private var storeAdapter: ConStoreAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<StoreModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.con_store)
        setSupportActionBar(toolbar_con_store)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        init()
    }

    private fun init() {
        var linearLayoutManager = LinearLayoutManager(this@ReqConStore)
        recyclerView = findViewById(R.id.rc_con_store)
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
                        list.add(addDataAll)
                        storeAdapter = ConStoreAdapter(this@ReqConStore, list)
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

}
