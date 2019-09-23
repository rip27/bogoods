package com.example.bogoods.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bogoods.R
import com.example.bogoods.adapter.ListBarangAdapter
import com.example.bogoods.adapter.ListRequestAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.ListBarangModel
import com.example.bogoods.model.ListRequestModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.list_request.*
import java.util.ArrayList

class ListRequest : AppCompatActivity() {


    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref

    private var recyclerView: RecyclerView? = null
    private var list: MutableList<ListRequestModel> = ArrayList()
    private var adapter: ListRequestAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_request)
        setSupportActionBar(toolbar_list_request)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

        showData()
    }
    private fun showData() {
        var linearLayoutManager = LinearLayoutManager(this@ListRequest)
        recyclerView = findViewById(R.id.rc_list_request)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)

        val idstore = intent.getStringExtra("idstore")
        fAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance()
            .reference.child("store/$idstore/requestconnection")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(
                        ListRequestModel::class.java
                    )
                    addDataAll!!.key = dataSnapshot.key
                    list.add(addDataAll)
                    adapter = ListRequestAdapter(this@ListRequest, list)
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
