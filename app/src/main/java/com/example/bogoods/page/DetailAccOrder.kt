package com.example.bogoods.page

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.detail_acc_order.*
import kotlinx.android.synthetic.main.detail_order.*

class DetailAccOrder : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var pref: Pref
    lateinit var fAuth: FirebaseAuth
    lateinit var imgup: ImageView
    lateinit var bt_up: TextView
    lateinit var bt_bayar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_acc_order)
        setSupportActionBar(toolbar_detail_acc_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val idorder = intent.getStringExtra("idorder")
        val orderRef = FirebaseDatabase.getInstance().getReference("order/$idorder")
        val storeRef = FirebaseDatabase.getInstance().getReference("store")
        val userRef = FirebaseDatabase.getInstance().getReference("user")
        pref = Pref(this)
        fAuth = FirebaseAuth.getInstance()
        orderRef.child("statuspembayaran").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    val statusbayar = p0.value.toString()
                    orderRef.child("statusbarang")
                        .addListenerForSingleValueEvent(
                            object : ValueEventListener {
                                override fun onCancelled(p1: DatabaseError) {

                                }

                                override fun onDataChange(p1: DataSnapshot) {
                                    val statusbarang = p1.value.toString()
                                    when (statusbayar) {
                                        "n" -> {
                                            status_barang_acc_detail.text = "Menunggu Pembayaran"
                                            status_bayar_acc.text = "Menunggu Pembayaran"
                                        }
                                        "p" -> {
                                            status_bayar_acc.text =
                                                "Menunggu Konfirmasi Pembayaran dari Admin"
                                            status_barang_acc_detail.text = "Menunggu Konfirmasi Pembayaran"
                                        }
                                        "konfirmasiseller" -> {
                                            status_bayar_acc.text =
                                                "Pembayaran Lunas"
                                            status_barang_acc_detail.text = "Menunggu Konfirmasi Pesanan dari Penjual"
                                        }
                                        "y" -> {
                                            status_bayar_acc.text = "Pembayaran Lunas"
                                            when (statusbarang) {
                                                "p" -> {
                                                    status_barang_acc_detail.text =
                                                        "Menunggu Konfirmasi Pengiriman"
                                                }
                                                "n" -> {
                                                    status_barang_acc_detail.text = "Pesanan Dibatalkan"
                                                }
                                                "y" -> {
                                                    status_barang_acc_detail.text = "Pesanan Sedang Dikirim"
                                                }
                                                "s" -> {
                                                    status_barang_acc_detail.text = "Pesanan Sudah Diterima"
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        )
                }

            }
        )
        orderRef.child("tglpesan").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val tgl = p0.value.toString()
                    tanggal_pesan_acc_detail.text = tgl
                }

            }
        )
        orderRef.child("jumlah").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    val jumlah = p0.value.toString()
                    jumlah_barang_acc_detail.text = "$jumlah pcs"
                }

            }
        )
        orderRef.child("total").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val total = p0.value.toString()
                    total_acc_detail.text = total
                }

            }
        )
        orderRef.child("totalbayar").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val total = p0.value.toString()
                    total_bayar_acc.text = total
                }

            }
        )
        orderRef.child("pembayaran").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val cok = p0.value.toString()
                    jenis_pembayaran_acc.text = cok
                }

            }
        )
        orderRef.child("idstore").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val idstore = p0.value.toString()
                    storeRef.child("$idstore/storename").addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                val namastore = p0.value.toString()
                                namatoko_acc_detail.text = namastore
                            }

                        }
                    )
                    storeRef.child("$idstore/address").addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                val add = p0.value.toString()
                                alamattoko_acc_detail.text = add
                            }

                        }
                    )
                    orderRef.child("idbarang").addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                val idbarang = p0.value.toString()
                                storeRef.child("$idstore/listbarang/$idbarang/imagebarang")
                                    .addListenerForSingleValueEvent(
                                        object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {
                                                val image = p0.value.toString()
                                                Glide.with(this@DetailAccOrder)
                                                    .load(image).centerCrop()
                                                    .into(image_barang_acc_detail)
                                            }

                                        }
                                    )
                                storeRef.child("$idstore/listbarang/$idbarang/namabarang")
                                    .addListenerForSingleValueEvent(
                                        object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {
                                                val nama = p0.value.toString()
                                                nama_barang_acc_detail.text = nama
                                            }

                                        }
                                    )
                                storeRef.child("$idstore/listbarang/$idbarang/harga")
                                    .addListenerForSingleValueEvent(
                                        object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            @SuppressLint("SetTextI18n")
                                            override fun onDataChange(p0: DataSnapshot) {
                                                val harga = p0.value.toString()
                                                harga_per_item_acc_detail.text = "Rp. $harga"
                                            }

                                        }
                                    )
                            }

                        }
                    )
                }

            }
        )
        orderRef.child("idpemilikstore").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val idpemilik = p0.value.toString()
                    userRef.child("$idpemilik/name").addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                val nama = p0.value.toString()
                                userRef.child("$idpemilik/phone").addListenerForSingleValueEvent(
                                    object : ValueEventListener {
                                        override fun onCancelled(p1: DatabaseError) {

                                        }

                                        @SuppressLint("SetTextI18n")
                                        override fun onDataChange(p1: DataSnapshot) {
                                            val phone = p1.value.toString()
                                            pemilik_toko_acc_detail.text = "$nama ( $phone ) "
                                        }
                                    }
                                )
                            }
                        }
                    )
                }

            }
        )
        orderRef.child("alamatpengiriman")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val alamat = p0.value.toString()
                        orderRef.child("idpembeli").addListenerForSingleValueEvent(
                            object : ValueEventListener {
                                override fun onCancelled(p1: DatabaseError) {

                                }

                                override fun onDataChange(p1: DataSnapshot) {
                                    val idpembeli = p1.value.toString()
                                    userRef.child("$idpembeli/name").addListenerForSingleValueEvent(
                                        object : ValueEventListener {
                                            override fun onCancelled(p2: DatabaseError) {

                                            }

                                            override fun onDataChange(p2: DataSnapshot) {
                                                val name = p2.value.toString()
                                                userRef.child("$idpembeli/phone")
                                                    .addListenerForSingleValueEvent(
                                                        object : ValueEventListener {
                                                            override fun onCancelled(p3: DatabaseError) {

                                                            }

                                                            @SuppressLint("SetTextI18n")
                                                            override fun onDataChange(p3: DataSnapshot) {
                                                                val phone = p3.value.toString()
                                                                alamat_pengiriman_acc_detail.text =
                                                                    "$name \n ( $phone ) \n $alamat"
                                                            }

                                                        }
                                                    )
                                            }

                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            )
        orderRef.child("pengiriman")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(p0: DataSnapshot) {
                        val kirim = p0.value.toString()
                        when (kirim) {
                            "menunggu" -> jenis_pengiriman_acc.text = "Menunggu Konfirmasi Penjual"
                            "paket" -> jenis_pengiriman_acc.text = "Jasa Paket"
                            "sendiri" -> jenis_pengiriman_acc.text = "Diantar Penjual"
                        }
                    }
                }
            )
    }
}
