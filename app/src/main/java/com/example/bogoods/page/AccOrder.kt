package com.example.bogoods.page

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bogoods.R
import com.example.bogoods.adapter.AccOrderAdapter
import com.example.bogoods.adapter.StoreAddOrderAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.OrderModel
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.acc_order.*
import kotlinx.android.synthetic.main.accept_request.*
import java.util.ArrayList

class AccOrder : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<OrderModel> = ArrayList()
    private var adapter: AccOrderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acc_order)
        setSupportActionBar(toolbar_acc_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        showData()

    }


    private fun showData() {
        var linearLayoutManager = LinearLayoutManager(this@AccOrder)
        recyclerView = findViewById(R.id.rc_acc_order)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)

        val idstore = intent.getStringExtra("idstore")
        fAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance()
            .reference.child("order/")
        dbRef.orderByChild("statuspembayaran").equalTo("konfirmasiseller")
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    list = ArrayList()
                    for (dataSnapshot in p0.children) {
                        val addDataAll = dataSnapshot.getValue(
                            OrderModel::class.java
                        )
                        addDataAll!!.key = dataSnapshot.key
                        if (addDataAll.idpemilikstore.toString() == fAuth.currentUser?.uid) {
                            list.add(addDataAll)
                        }
                        adapter = AccOrderAdapter(this@AccOrder, list)
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
