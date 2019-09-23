package com.example.bogoods.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bogoods.R
import com.example.bogoods.adapter.AccStoreAdapter
import com.example.bogoods.adapter.ConStoreAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.accept_request.*
import kotlinx.android.synthetic.main.con_store.*
import java.util.ArrayList

class AcceptRequest : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref
    private var storeAdapter: AccStoreAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<StoreModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accept_request)

        setSupportActionBar(toolbar_acc_req)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        init()
    }

    private fun init() {
        var linearLayoutManager = LinearLayoutManager(this@AcceptRequest)
        recyclerView = findViewById(R.id.rc_acc_req)
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
                        storeAdapter = AccStoreAdapter(this@AcceptRequest, list)
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
