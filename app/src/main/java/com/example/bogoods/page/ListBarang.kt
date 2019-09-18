package com.example.bogoods.page

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bogoods.R
import com.example.bogoods.adapter.ListBarangAdapter
import com.example.bogoods.adapter.StoreAdapter
import com.example.bogoods.data.Pref
import com.example.bogoods.model.ListBarangModel
import com.example.bogoods.model.StoreModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.list_barang.*
import kotlinx.android.synthetic.main.store.*
import java.util.*

class ListBarang : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<ListBarangModel> = ArrayList()
    private var listBarangAdapter: ListBarangAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_barang)
        setSupportActionBar(toolbar_list_barang)
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        val idstore = intent.getStringExtra("idstore")
        supportActionBar!!.title = idstore
        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

        val uid = fAuth.currentUser?.uid

        showData()

        add_barang.setOnClickListener {
            showDialogAddBarang()
        }

    }

    private fun showData() {
        var linearLayoutManager = LinearLayoutManager(this@ListBarang)
        recyclerView = findViewById(R.id.rc_barang)
        recyclerView!!.layoutManager = linearLayoutManager
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
                    listBarangAdapter = ListBarangAdapter(this@ListBarang, list)
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

    private fun showDialogAddBarang() {
        var dialog: AlertDialog
        val alertDialog = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.add_barang, null)
        alertDialog.setView(view)
        alertDialog.setTitle("DAFTAR")
        alertDialog.setPositiveButton("DAFTAR") { dialog, i ->
            val namabarang = view.findViewById<EditText>(R.id.et_nama_barang).text.toString()
            val harga = view.findViewById<EditText>(R.id.et_harga).text.toString()
            val stok = view.findViewById<EditText>(R.id.et_harga).text.toString()
            if (namabarang.isEmpty() || harga.isEmpty() || stok.isEmpty()) {
                Toast.makeText(this, "Fill All Data", Toast.LENGTH_SHORT).show()
            } else {
                addBarang(namabarang, harga, stok)
            }
        }
        alertDialog.setNegativeButton("NO") { dialog, i ->
            dialog.dismiss()
        }
        dialog = alertDialog.create()
        dialog.show()
    }

    private fun addBarang(namabarang: String, harga: String, stok: String) {
        val nameXXX = UUID.randomUUID().toString()
        val idbarang = UUID.randomUUID().toString()
        val idstore = intent.getStringExtra("idstore")
        val uid = fAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("store/$idstore/listbarang")
        dbRef.child("idbarang").setValue(idbarang)
        dbRef.child("idstore").setValue(idstore)
        dbRef.child("namabarang").setValue(namabarang)
        dbRef.child("stok").setValue(stok)
        dbRef.child("harga").setValue(harga)
        dbRef.child("idpemilik").setValue(uid)
        Toast.makeText(
            this,
            "Sukses Daftar Toko Mohon Tunggu Konfirmasi dari Admin",
            Toast.LENGTH_SHORT
        ).show()
    }

}
