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
import android.widget.*
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
    lateinit var sppilihp: Spinner
    lateinit var bt_pilih: Button

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
        orderRef.child("pembayaran").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(pek: DatabaseError) {

                }

                override fun onDataChange(pek: DataSnapshot) {
                    val jenispembayaran = pek.value.toString()
                    jenis_pembayaran_acc.text = jenispembayaran
                    when (jenispembayaran) {
                        "Transfer BCA" -> {
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
                                                                status_barang_acc_detail.text =
                                                                    "Menunggu Konfirmasi Pembayaran"
                                                            }
                                                            "konfirmasiseller" -> {
                                                                status_bayar_acc.text = "Pembayaran Lunas"
                                                                when (statusbarang) {
                                                                    "p" -> {
                                                                        jenis_pengiriman_acc.text =
                                                                            "Menunggu Konfirmasi Pesanan"
                                                                        status_barang_acc_detail.text =
                                                                            "Menunggu Konfirmasi Pesanan"
                                                                        bt_terima_order.setOnClickListener {
                                                                            var dialog: android.app.AlertDialog
                                                                            val alertDialog =
                                                                                android.app.AlertDialog.Builder(this@DetailAccOrder)
                                                                            alertDialog.setTitle("Terima Pesanan")
                                                                            alertDialog.setPositiveButton("Terima") { dialog, i ->
                                                                                orderRef.child("idstore")
                                                                                    .addListenerForSingleValueEvent(
                                                                                        object : ValueEventListener {
                                                                                            override fun onCancelled(p0: DatabaseError) {

                                                                                            }

                                                                                            override fun onDataChange(p0: DataSnapshot) {
                                                                                                val idstore =
                                                                                                    p0.value.toString()
                                                                                                orderRef.child("idbarang")
                                                                                                    .addListenerForSingleValueEvent(
                                                                                                        object :
                                                                                                            ValueEventListener {
                                                                                                            override fun onCancelled(
                                                                                                                p01: DatabaseError
                                                                                                            ) {


                                                                                                            }

                                                                                                            override fun onDataChange(
                                                                                                                p01: DataSnapshot
                                                                                                            ) {
                                                                                                                val idbarang =
                                                                                                                    p01.value.toString()
                                                                                                                storeRef.child(
                                                                                                                    "$idstore/listbarang/$idbarang/stok"
                                                                                                                )
                                                                                                                    .addListenerForSingleValueEvent(
                                                                                                                        object :
                                                                                                                            ValueEventListener {
                                                                                                                            override fun onCancelled(
                                                                                                                                p02: DatabaseError
                                                                                                                            ) {

                                                                                                                            }

                                                                                                                            override fun onDataChange(
                                                                                                                                p02: DataSnapshot
                                                                                                                            ) {
                                                                                                                                val stok =
                                                                                                                                    p02.value.toString()
                                                                                                                                orderRef.child(
                                                                                                                                    "jumlah"
                                                                                                                                )
                                                                                                                                    .addListenerForSingleValueEvent(
                                                                                                                                        object :
                                                                                                                                            ValueEventListener {
                                                                                                                                            override fun onCancelled(
                                                                                                                                                p03: DatabaseError
                                                                                                                                            ) {

                                                                                                                                            }

                                                                                                                                            override fun onDataChange(
                                                                                                                                                p03: DataSnapshot
                                                                                                                                            ) {
                                                                                                                                                val jumlah =
                                                                                                                                                    p03.value.toString()
                                                                                                                                                val tersedia =
                                                                                                                                                    (stok.toInt() - jumlah.toInt()).toString()
                                                                                                                                                storeRef.child(
                                                                                                                                                    "$idstore/listbarang/$idbarang/stok"
                                                                                                                                                )
                                                                                                                                                    .setValue(
                                                                                                                                                        tersedia
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
                                                                                            }
                                                                                        }
                                                                                    )
                                                                                orderRef.child("statusbarang")
                                                                                    .setValue("konfirm")
                                                                                bt_terima_order.visibility = View.GONE
                                                                                bt_tolak_order.visibility = View.GONE
                                                                                Toast.makeText(
                                                                                    this@DetailAccOrder,
                                                                                    "Sukses Terima",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                            }
                                                                            alertDialog.setNegativeButton("Cancel") { dialog, i ->
                                                                                dialog.dismiss()
                                                                            }
                                                                            dialog = alertDialog.create()
                                                                            dialog.show()
                                                                        }
                                                                        bt_tolak_order.setOnClickListener {
                                                                            var dialog: android.app.AlertDialog
                                                                            val alertDialog =
                                                                                android.app.AlertDialog.Builder(this@DetailAccOrder)
                                                                            alertDialog.setTitle("Tolak Pesanan")
                                                                            alertDialog.setPositiveButton("Tolak") { dialog, i ->
                                                                                orderRef.child("statusbarang")
                                                                                    .setValue("n")
                                                                                bt_terima_order.visibility = View.GONE
                                                                                bt_tolak_order.visibility = View.GONE
                                                                                Toast.makeText(
                                                                                    this@DetailAccOrder,
                                                                                    "Sukses Tolak",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                            }
                                                                            alertDialog.setNegativeButton("Cancel") { dialog, i ->
                                                                                dialog.dismiss()
                                                                            }
                                                                            dialog = alertDialog.create()
                                                                            dialog.show()
                                                                        }
                                                                    }

                                                                    "konfirm" -> {
                                                                        bt_terima_order.visibility = View.GONE
                                                                        bt_tolak_order.visibility = View.GONE
                                                                        c_status_pesanan_terima.visibility =
                                                                            View.VISIBLE
                                                                        c_status_pesanan_tolak.visibility = View.GONE
                                                                        status_barang_acc_detail.text =
                                                                            "Pesanan Terkonfirmasi"
                                                                        orderRef.child("pengiriman")
                                                                            .addListenerForSingleValueEvent(
                                                                                object : ValueEventListener {
                                                                                    override fun onCancelled(pok: DatabaseError) {

                                                                                    }

                                                                                    @SuppressLint("SetTextI18n")
                                                                                    override fun onDataChange(pok: DataSnapshot) {
                                                                                        jenis_pengiriman_acc.visibility =
                                                                                            View.GONE
                                                                                        bt_pilih_pengiriman.visibility =
                                                                                            View.VISIBLE
                                                                                        bt_pilih_pengiriman.setOnClickListener {
                                                                                            showDialogPilihPengiriman()
                                                                                        }
                                                                                    }
                                                                                }
                                                                            )
                                                                    }
                                                                    "n" -> {
                                                                        status_barang_acc_detail.text =
                                                                            "Pesanan Dibatalkan"
                                                                        bt_terima_order.visibility = View.GONE
                                                                        bt_tolak_order.visibility = View.GONE
                                                                        c_status_pesanan_terima.visibility = View.GONE
                                                                        c_status_pesanan_tolak.visibility = View.VISIBLE
                                                                    }
                                                                    "y" -> {
                                                                        bt_terima_order.visibility = View.GONE
                                                                        bt_tolak_order.visibility = View.GONE
                                                                        c_status_pesanan_terima.visibility =
                                                                            View.VISIBLE
                                                                        tv_pesanan_dikonfirmasi.text =
                                                                            "Pesanan Sedang Dikirim"
                                                                        c_status_pesanan_tolak.visibility = View.GONE
                                                                        status_barang_acc_detail.text =
                                                                            "Pesanan Sedang Dikirim"
                                                                        orderRef.child("pengiriman")
                                                                            .addListenerForSingleValueEvent(
                                                                                object : ValueEventListener {
                                                                                    override fun onCancelled(pok: DatabaseError) {

                                                                                    }

                                                                                    @SuppressLint("SetTextI18n")
                                                                                    override fun onDataChange(pok: DataSnapshot) {
                                                                                        when (pok.value.toString()) {
                                                                                            "Jasa Paket" -> {
                                                                                                jenis_pengiriman_acc.visibility =
                                                                                                    View.VISIBLE
                                                                                                bt_pilih_pengiriman.visibility =
                                                                                                    View.GONE
                                                                                                jenis_pengiriman_acc.text =
                                                                                                    "Jasa Paket"
                                                                                            }
                                                                                            "Diantar Penjual" -> {

                                                                                                jenis_pengiriman_acc.visibility =
                                                                                                    View.VISIBLE
                                                                                                bt_pilih_pengiriman.visibility =
                                                                                                    View.GONE
                                                                                                jenis_pengiriman_acc.text =
                                                                                                    "Diantar Penjual"
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            )
                                                                        bt_pesanan_sudah_sampai.visibility =
                                                                            View.VISIBLE
                                                                        bt_pesanan_sudah_sampai.setOnClickListener {
                                                                            var dialog: android.app.AlertDialog
                                                                            val alertDialog =
                                                                                android.app.AlertDialog.Builder(this@DetailAccOrder)
                                                                            alertDialog.setTitle("Pesanan Sampai ?")
                                                                            alertDialog.setPositiveButton("Ya") { dialog, i ->
                                                                                orderRef.child("statusbarang")
                                                                                    .setValue("s")
                                                                                bt_pesanan_sudah_sampai.visibility =
                                                                                    View.GONE
                                                                            }
                                                                            alertDialog.setNegativeButton("Batal") { dialog, i ->
                                                                                dialog.dismiss()
                                                                            }
                                                                            dialog = alertDialog.create()
                                                                            dialog.show()
                                                                        }
                                                                    }
                                                                    "s" -> {
                                                                        bt_pesanan_sudah_sampai.visibility = View.GONE
                                                                        bt_terima_order.visibility = View.GONE
                                                                        bt_tolak_order.visibility = View.GONE
                                                                        c_status_pesanan_terima.visibility =
                                                                            View.VISIBLE
                                                                        tv_pesanan_dikonfirmasi.text =
                                                                            "Pesanan Telah Sampai"
                                                                        c_status_pesanan_tolak.visibility = View.GONE
                                                                        orderRef.child("pengiriman")
                                                                            .addListenerForSingleValueEvent(
                                                                                object : ValueEventListener {
                                                                                    override fun onCancelled(pok: DatabaseError) {

                                                                                    }

                                                                                    @SuppressLint("SetTextI18n")
                                                                                    override fun onDataChange(pok: DataSnapshot) {
                                                                                        when (pok.value.toString()) {
                                                                                            "Jasa Paket" -> {
                                                                                                jenis_pengiriman_acc.visibility =
                                                                                                    View.VISIBLE
                                                                                                bt_pilih_pengiriman.visibility =
                                                                                                    View.GONE
                                                                                                jenis_pengiriman_acc.text =
                                                                                                    "Jasa Paket"
                                                                                            }
                                                                                            "Diantar Penjual" -> {
                                                                                                jenis_pengiriman_acc.visibility =
                                                                                                    View.VISIBLE
                                                                                                bt_pilih_pengiriman.visibility =
                                                                                                    View.GONE
                                                                                                jenis_pengiriman_acc.text =
                                                                                                    "Diantar Penjual"
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            )
                                                                        status_barang_acc_detail.text =
                                                                            "Pesanan Sudah Diterima"
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
                        }
                        "Bayar di Tempat" -> {
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
                                                            "konfirmasiseller" -> {
                                                                status_bayar_acc.text = "Belum Lunas"
                                                                when (statusbarang) {
                                                                    "p" -> {
                                                                        jenis_pengiriman_acc.text =
                                                                            "Menunggu Konfirmasi Pesanan"
                                                                        status_barang_acc_detail.text =
                                                                            "Menunggu Konfirmasi Pesanan"
                                                                        bt_terima_order.setOnClickListener {
                                                                            var dialog: android.app.AlertDialog
                                                                            val alertDialog =
                                                                                android.app.AlertDialog.Builder(this@DetailAccOrder)
                                                                            alertDialog.setTitle("Terima Pesanan")
                                                                            alertDialog.setPositiveButton("Terima") { dialog, i ->
                                                                                orderRef.child("statusbarang")
                                                                                    .setValue("konfirm")
                                                                                bt_terima_order.visibility = View.GONE
                                                                                bt_tolak_order.visibility = View.GONE
                                                                                Toast.makeText(
                                                                                    this@DetailAccOrder,
                                                                                    "Sukses Terima",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                            }
                                                                            alertDialog.setNegativeButton("Cancel") { dialog, i ->
                                                                                dialog.dismiss()
                                                                            }
                                                                            dialog = alertDialog.create()
                                                                            dialog.show()
                                                                        }
                                                                        bt_tolak_order.setOnClickListener {
                                                                            var dialog: android.app.AlertDialog
                                                                            val alertDialog =
                                                                                android.app.AlertDialog.Builder(this@DetailAccOrder)
                                                                            alertDialog.setTitle("Tolak Pesanan")
                                                                            alertDialog.setPositiveButton("Tolak") { dialog, i ->
                                                                                orderRef.child("statusbarang")
                                                                                    .setValue("n")
                                                                                bt_terima_order.visibility = View.GONE
                                                                                bt_tolak_order.visibility = View.GONE
                                                                                Toast.makeText(
                                                                                    this@DetailAccOrder,
                                                                                    "Sukses Tolak",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                            }
                                                                            alertDialog.setNegativeButton("Cancel") { dialog, i ->
                                                                                dialog.dismiss()
                                                                            }
                                                                            dialog = alertDialog.create()
                                                                            dialog.show()
                                                                        }
                                                                    }
                                                                    "konfirm" -> {
                                                                        bt_terima_order.visibility = View.GONE
                                                                        bt_tolak_order.visibility = View.GONE
                                                                        c_status_pesanan_terima.visibility =
                                                                            View.VISIBLE
                                                                        c_status_pesanan_tolak.visibility = View.GONE
                                                                        status_barang_acc_detail.text =
                                                                            "Pesanan Terkonfirmasi"
                                                                        orderRef.child("pengiriman")
                                                                            .addListenerForSingleValueEvent(
                                                                                object : ValueEventListener {
                                                                                    override fun onCancelled(pok: DatabaseError) {

                                                                                    }

                                                                                    @SuppressLint("SetTextI18n")
                                                                                    override fun onDataChange(pok: DataSnapshot) {
                                                                                        jenis_pengiriman_acc.visibility =
                                                                                            View.GONE
                                                                                        bt_pilih_pengiriman.visibility =
                                                                                            View.VISIBLE
                                                                                        bt_pilih_pengiriman.setOnClickListener {
                                                                                            showDialogPilihPengiriman()
                                                                                        }
                                                                                    }
                                                                                }
                                                                            )
                                                                    }
                                                                    "n" -> {
                                                                        status_barang_acc_detail.text =
                                                                            "Pesanan Dibatalkan"
                                                                        bt_terima_order.visibility = View.GONE
                                                                        bt_tolak_order.visibility = View.GONE
                                                                        c_status_pesanan_terima.visibility = View.GONE
                                                                        c_status_pesanan_tolak.visibility = View.VISIBLE
                                                                    }
                                                                    "y" -> {
                                                                        bt_terima_order.visibility = View.GONE
                                                                        bt_tolak_order.visibility = View.GONE
                                                                        c_status_pesanan_terima.visibility =
                                                                            View.VISIBLE
                                                                        tv_pesanan_dikonfirmasi.text =
                                                                            "Pesanan Sedang Dikirim"
                                                                        c_status_pesanan_tolak.visibility = View.GONE
                                                                        status_barang_acc_detail.text =
                                                                            "Pesanan Sedang Dikirim"
                                                                        orderRef.child("pengiriman")
                                                                            .addListenerForSingleValueEvent(
                                                                                object : ValueEventListener {
                                                                                    override fun onCancelled(pok: DatabaseError) {

                                                                                    }

                                                                                    @SuppressLint("SetTextI18n")
                                                                                    override fun onDataChange(pok: DataSnapshot) {
                                                                                        when (pok.value.toString()) {
                                                                                            "Jasa Paket" -> {
                                                                                                jenis_pengiriman_acc.visibility =
                                                                                                    View.VISIBLE
                                                                                                bt_pilih_pengiriman.visibility =
                                                                                                    View.GONE
                                                                                                jenis_pengiriman_acc.text =
                                                                                                    "Jasa Paket"
                                                                                            }
                                                                                            "Diantar Penjual" -> {

                                                                                                jenis_pengiriman_acc.visibility =
                                                                                                    View.VISIBLE
                                                                                                bt_pilih_pengiriman.visibility =
                                                                                                    View.GONE
                                                                                                jenis_pengiriman_acc.text =
                                                                                                    "Diantar Penjual"
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            )
                                                                        bt_pesanan_sudah_sampai.visibility =
                                                                            View.VISIBLE
                                                                        bt_pesanan_sudah_sampai.setOnClickListener {
                                                                            var dialog: android.app.AlertDialog
                                                                            val alertDialog =
                                                                                android.app.AlertDialog.Builder(this@DetailAccOrder)
                                                                            alertDialog.setTitle("Pesanan Sampai ?")
                                                                            alertDialog.setPositiveButton("Ya") { dialog, i ->
                                                                                orderRef.child("statusbarang")
                                                                                    .setValue("s")
                                                                                orderRef.child("statusbarang")
                                                                                    .setValue("s")
                                                                                bt_pesanan_sudah_sampai.visibility =
                                                                                    View.GONE
                                                                            }
                                                                            alertDialog.setNegativeButton("Batal") { dialog, i ->
                                                                                dialog.dismiss()
                                                                            }
                                                                            dialog = alertDialog.create()
                                                                            dialog.show()
                                                                        }
                                                                    }
                                                                    "s" -> {
                                                                        bt_pesanan_sudah_sampai.visibility = View.GONE
                                                                        bt_terima_order.visibility = View.GONE
                                                                        bt_tolak_order.visibility = View.GONE
                                                                        c_status_pesanan_terima.visibility =
                                                                            View.VISIBLE
                                                                        tv_pesanan_dikonfirmasi.text =
                                                                            "Pesanan Telah Sampai"
                                                                        c_status_pesanan_tolak.visibility = View.GONE
                                                                        orderRef.child("pengiriman")
                                                                            .addListenerForSingleValueEvent(
                                                                                object : ValueEventListener {
                                                                                    override fun onCancelled(pok: DatabaseError) {

                                                                                    }

                                                                                    @SuppressLint("SetTextI18n")
                                                                                    override fun onDataChange(pok: DataSnapshot) {
                                                                                        when (pok.value.toString()) {
                                                                                            "Jasa Paket" -> {
                                                                                                jenis_pengiriman_acc.visibility =
                                                                                                    View.VISIBLE
                                                                                                bt_pilih_pengiriman.visibility =
                                                                                                    View.GONE
                                                                                                jenis_pengiriman_acc.text =
                                                                                                    "Jasa Paket"
                                                                                            }
                                                                                            "Diantar Penjual" -> {
                                                                                                jenis_pengiriman_acc.visibility =
                                                                                                    View.VISIBLE
                                                                                                bt_pilih_pengiriman.visibility =
                                                                                                    View.GONE
                                                                                                jenis_pengiriman_acc.text =
                                                                                                    "Diantar Penjual"
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            )
                                                                        status_barang_acc_detail.text =
                                                                            "Pesanan Sudah Diterima"
                                                                        status_bayar_acc.text = "Pembayaran Lunas"
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
                        }
                    }
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
                                                                nama_pembeli_acc_detail.text = "$name ( $phone )"
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
    }

    private fun showDialogPilihPengiriman() {
        var dialog: android.app.AlertDialog
        val alertDialog =
            android.app.AlertDialog.Builder(this@DetailAccOrder)
        val view = LayoutInflater.from(this@DetailAccOrder)
            .inflate(R.layout.popup_pilih_pengiriman, null)
        alertDialog.setView(view)
        alertDialog.setTitle("Pilih Pengiriman")
        dialog = alertDialog.create()
        val idorder = intent.getStringExtra("idorder")
        val orderRef = FirebaseDatabase.getInstance().getReference("order/$idorder")
        sppilihp = view.findViewById(R.id.pilih_metode_pengiriman)
        bt_pilih = view.findViewById(R.id.bt_pilih_pengiriman_popup)

        bt_pilih.setOnClickListener {
            val pengiriman = sppilihp.selectedItem.toString()
            orderRef.child("pengiriman")
                .setValue(pengiriman)
            orderRef.child("statusbarang")
                .setValue("y")
            dialog.dismiss()
        }
        dialog.show()
    }
}
