package com.example.bogoods.page

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.adapter.CartAdapter
import com.example.bogoods.adapter.RatingAdapter
import com.example.bogoods.adapter.StoreAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.CartModel
import com.example.bogoods.model.RatingModel
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.detail_barang.*
import kotlinx.android.synthetic.main.edit_barang.*
import java.util.*

class DetailBarang : AppCompatActivity() {


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
        setContentView(R.layout.detail_barang)

        setSupportActionBar(toolbar_detail_barang)
        supportActionBar!!.title = "Detail Barang"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pref = Pref(this)
        fAuth = FirebaseAuth.getInstance()

        val idstore = intent.getStringExtra("idstore")
        val idbarang = intent.getStringExtra("idbarang")

// Rating
        var linearLayoutManager = LinearLayoutManager(this@DetailBarang)
        recyclerView = findViewById(R.id.rc_rating)
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
                    adapter = RatingAdapter(this@DetailBarang, list)
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


        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("imagebarang").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Glide.with(this@DetailBarang)
                            .load(p0.value.toString())
                            .error(R.drawable.ic_reseller)
                            .into(detail_gambar_barang)
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("namabarang").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        nama_barang_detail.setText(p0.value.toString())
                    }

                }
            )
        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("desc").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        tv_desc_detail.setText(p0.value.toString())
                    }

                }
            )
        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("harga").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        tv_harga_detail.setText("Rp. " + p0.value.toString())
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("stok").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val stok = p0.value.toString()
                        tv_stok_detail.text = "$stok pcs"
                        if (stok.toInt() <= 0) {
                            masukin.text = "Stok Habis"
                        } else {
                            add_to_cart.setOnClickListener {
                                showDialogCart()
                            }
                        }
                    }

                }
            )
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
                        rating += it.child("rating").value.toString().toFloat()
                        i++
                        rating_barang_number.text = "Rating\n${rating/i} dari $i Ulasan"
                    }
                }
            }
        )
    }

    private fun showDialogCart() {
        var dialog: AlertDialog
        val alertDialog = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.popup_cart, null)
        alertDialog.setView(view)
        alertDialog.setTitle("Masukan Keranjang")
        val idstore = intent.getStringExtra("idstore")
        val idbarang = intent.getStringExtra("idbarang")
        val stok = view.findViewById<TextView>(R.id.tv_stok_dinamis)
        val tv_stok = view.findViewById<TextView>(R.id.tv_stok_popup)
        FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang/$idbarang")
            .child("stok").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        tv_stok.setText(p0.value.toString())
                    }

                }
            )
        btplus = view.findViewById(R.id.bt_plus_stok)
        btmin = view.findViewById(R.id.bt_min_stok)
        btplus.setOnClickListener {
            if (tv_stok.text.toString().toInt() == stok.text.toString().toInt()) {
                Toast.makeText(this, "Stok Habis", Toast.LENGTH_SHORT).show()
            } else {
                stok.text = (stok.text.toString().toInt() + 1).toString()
            }
        }
        btmin.setOnClickListener {
            if (stok.text.toString().toInt() == 1) {
                Toast.makeText(this, "Tambah Stok", Toast.LENGTH_SHORT).show()
            } else {
                stok.text = (stok.text.toString().toInt() - 1).toString()
            }
        }
        alertDialog.setPositiveButton("TAMBAH") { dialog, i ->
            if (stok.text.toString().toInt() < 1) {
                Toast.makeText(this, "Tambah Stok", Toast.LENGTH_SHORT).show()
            } else {
                addToCart(stok.text.toString())
            }
        }
        alertDialog.setNegativeButton("NO") { dialog, i ->
            dialog.dismiss()
        }
        dialog = alertDialog.create()
        dialog.show()
    }

    fun addToCart(stok: String) {

        val idstore = intent.getStringExtra("idstore")
        val idbarang = intent.getStringExtra("idbarang")


        dbRef = FirebaseDatabase.getInstance().reference.child("cart/$idbarang")

        Toast.makeText(this, "Sukses Masuk", Toast.LENGTH_SHORT).show()
        FirebaseDatabase.getInstance()
            .reference.child("cart/$idbarang/idbarang").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    FirebaseDatabase.getInstance().getReference("cart/$idbarang/jumlah")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                val jumlah = p0.value.toString().toInt()
                                dbRef.child("jumlah").setValue((jumlah + stok.toInt()).toString())
                            }

                        })
                } else {
                    FirebaseDatabase.getInstance().getReference("store/$idstore/idpemilik")
                        .addListenerForSingleValueEvent(
                            object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    dbRef.child("idpemilikstore").setValue(p0.value.toString())
                                }
                            }
                        )
                    dbRef.child("idcart").setValue(idbarang)
                    dbRef.child("idbarang").setValue(idbarang)
                    dbRef.child("idstore").setValue(idstore)
                    dbRef.child("jumlah").setValue(stok)
                    dbRef.child("idpembeli").setValue(fAuth.currentUser?.uid)
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
