package com.example.bogoods.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.adapter.RatingAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.RatingModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.detail_barang.*
import kotlinx.android.synthetic.main.detail_list_barang.*
import java.util.ArrayList

class DetailListBarang : AppCompatActivity() {

    lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var pref: Pref
    lateinit var btplus: RelativeLayout
    lateinit var btmin: RelativeLayout

    private var adapter: RatingAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<RatingModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_list_barang)
        setSupportActionBar(toolbar_detail_list_barang)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val idstore = intent.getStringExtra("idstore")
        val idbarang = intent.getStringExtra("idbarang")

        var linearLayoutManager = LinearLayoutManager(this@DetailListBarang)
        recyclerView = findViewById(R.id.rc_rating_list_barang)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)

        fAuth = FirebaseAuth.getInstance()

        val ratingRef = FirebaseDatabase.getInstance()
            .reference.child("store/$idstore/listbarang/$idbarang/rating")
        ratingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(
                        RatingModel::class.java
                    )
                    addDataAll!!.key = dataSnapshot.key
                    list.add(addDataAll)
                    adapter = RatingAdapter(this@DetailListBarang, list)
                    recyclerView!!.adapter = adapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(
                    "TAG_ERROR", p0.message
                )
            }
        })

        total()

        val barangRef = FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
        barangRef.child("imagebarang").addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    Glide.with(this@DetailListBarang).load(p0.value.toString())
                        .centerCrop().into(imagebarang_list_detail)
                }

            }
        )
        barangRef.child("namabarang").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    nama_barang_list_detail.text = p0.value.toString()
                }
            }
        )
        barangRef.child("harga").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    harga_list_detail.text = "Rp. ${p0.value.toString()}"
                }
            }
        )
        barangRef.child("stok").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    stok_list_detail.text = p0.value.toString()
                }
            }
        )
        barangRef.child("desc").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    desc_list_detail.text = p0.value.toString()
                }
            }
        )
        FirebaseDatabase.getInstance().getReference("store")
            .child("$idstore/storename").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    namatoko_list_detail.text = p0.value.toString()
                }
            }
        )
        ic_edit_bondo.setOnClickListener {

            val intent = Intent(this@DetailListBarang, EditBarang::class.java)
            intent.putExtra("idstore", idstore)
            intent.putExtra("idbarang", idbarang)
            startActivity(intent)
        }
    }
    fun total() {
        val idstore = intent.getStringExtra("idstore")
        val idbarang = intent.getStringExtra("idbarang")
        val ratingRef = FirebaseDatabase.getInstance()
            .reference.child("store/$idstore/listbarang/$idbarang/rating")
        ratingRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var i = 0
                    var rating = 0f
                    p0.children.forEach {
                        rating+=rating + it.child("rating").value.toString().toFloat()
                        i++
                    }
                    if (rating.toInt() == 0){
                        rating_list_barang_number.text = "Rating\n0 dari $i Ulasan"
                    }else{
                        rating_list_barang_number.text = "Rating\n${(rating/i)} dari $i Ulasan"
                    }
                }
            }
        )
    }
}
