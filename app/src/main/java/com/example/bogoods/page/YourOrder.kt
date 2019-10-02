package com.example.bogoods.page

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bogoods.R
import com.example.bogoods.adapter.OrderAdapter
import com.example.bogoods.adapter.StoreAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.OrderModel
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.store.*
import kotlinx.android.synthetic.main.your_order.*
import java.util.ArrayList

class YourOrder : AppCompatActivity() {


    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    var value = 0.0
    lateinit var filePathImage: Uri
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference
    private var adapter: OrderAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<OrderModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.your_order)
        setSupportActionBar(toolbar_your_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

        val uid = fAuth.currentUser?.uid

        add_order.setOnClickListener {
            startActivity(Intent(this@YourOrder, AddOrder::class.java))
        }

        init()

    }

    private fun init() {
        var linearLayoutManager = LinearLayoutManager(this@YourOrder)
        recyclerView = findViewById(R.id.rc_your_order)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)

        fAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance()
            .reference.child("order")
        dbRef.orderByChild("idpembeli").equalTo(fAuth.currentUser?.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    list = ArrayList()
                    for (dataSnapshot in p0.children) {
                        val addDataAll = dataSnapshot.getValue(
                            OrderModel::class.java
                        )
                        addDataAll!!.key = dataSnapshot.key
                        list.add(addDataAll)
                    }
                    adapter = OrderAdapter(this@YourOrder, list)
                    recyclerView!!.adapter = adapter
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e(
                        "TAG_ERROR", p0.message
                    )
                }
            })
    }
}
