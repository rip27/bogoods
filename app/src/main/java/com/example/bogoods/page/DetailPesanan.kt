package com.example.bogoods.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.detail_barang.*
import kotlinx.android.synthetic.main.detail_pesanan.*

class DetailPesanan : AppCompatActivity() {

    lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_pesanan)


        setSupportActionBar(toolbar_detail_pesanan)
        supportActionBar!!.title = "Detail Pesanan"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pref = Pref(this)
        fAuth = FirebaseAuth.getInstance()



    }
}
