package com.example.bogoods.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bogoods.R
import com.example.bogoods.adapter.ListBarangAddOrderAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.ListBarangModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.list_barang_add_order.*
import java.util.*

class ListBarangAddOrder : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref
    var value = 0.0
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<ListBarangModel> = ArrayList()
    private var listBarangAdapter: ListBarangAddOrderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_barang_add_order)
        setSupportActionBar(toolbar_list_barang_add_order)
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
        FirebaseDatabase.getInstance().getReference("cart").orderByChild("idpembeli").equalTo(fAuth.currentUser?.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    count_cart.text = p0.childrenCount.toString()
                }

                override fun onCancelled(p0: DatabaseError) {
                }

            })
        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

        val uid = fAuth.currentUser?.uid

        showData()

    }

    private fun showData() {
        recyclerView = findViewById(R.id.rc_barang_add_order)
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
                    listBarangAdapter = ListBarangAddOrderAdapter(this@ListBarangAddOrder, list)
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
}
