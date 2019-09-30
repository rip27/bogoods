package com.example.bogoods.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bogoods.R
import com.example.bogoods.adapter.CartAdapter
import com.example.bogoods.adapter.StoreAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.CartModel
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.detail_barang.*
import kotlinx.android.synthetic.main.detail_pesanan.*
import java.util.ArrayList

class DetailPesanan : AppCompatActivity() {

    lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var pref: Pref

    private var adapter: CartAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<CartModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_pesanan)


        setSupportActionBar(toolbar_detail_pesanan)
        supportActionBar!!.title = "Detail Pesanan"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pref = Pref(this)
        fAuth = FirebaseAuth.getInstance()

        init()

    }

    private fun init() {
        var linearLayoutManager = LinearLayoutManager(this@DetailPesanan)
        recyclerView = findViewById(R.id.rc_cart)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)

        fAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance()
            .reference.child("cart")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
//                list = ArrayList()
//                val listX = ArrayList<String>()
//                loop@ for (dataSnapshot in p0.children) {
//                    val addDataAll = dataSnapshot.getValue(
//                        CartModel::class.java
//                    )
//
//                    if (addDataAll!!.idpembeli.toString() == fAuth.currentUser?.uid) {
//                        addDataAll.key = dataSnapshot.key
//                        for (key in listX) {
//                            if (key == addDataAll.key) {
//                                continue@loop
//                            }
//                        }
//                        for (dataSnapshot2 in p0.children) {
//                            val addDataAll2 = dataSnapshot2.getValue(
//                                CartModel::class.java
//                            )
//                            if (addDataAll.idbarang == addDataAll2!!.idbarang &&
//                                addDataAll.idpembeli.toString() == fAuth.currentUser?.uid &&
//                                addDataAll.key != dataSnapshot2.key
//                            ) {
//                                e("jumlah", "${addDataAll.jumlah} + ${addDataAll2.jumlah} ${dataSnapshot2.key}")
//                                addDataAll.jumlah =
//                                    (addDataAll.jumlah!!.toInt() + addDataAll2.jumlah!!.toInt()).toString()
//                                listX.add(dataSnapshot2.key!!)
//                            }
//                        }
//                        list.add(addDataAll)
//                    }
//                }
                list = ArrayList()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(
                        CartModel::class.java
                    )
                    addDataAll!!.key = dataSnapshot.key
                    if (addDataAll.idpembeli.toString() == fAuth.currentUser?.uid) {
                        list.add(addDataAll)
                    }
                }
                adapter = CartAdapter(this@DetailPesanan, list)
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
