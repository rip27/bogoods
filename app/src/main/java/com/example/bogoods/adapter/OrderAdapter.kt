package com.example.bogoods.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Image
import android.util.Log
import com.example.bogoods.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bogoods.data.Pref
import com.example.bogoods.model.*
import com.example.bogoods.page.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    lateinit var mCtx: Context
    lateinit var item: List<OrderModel>
    lateinit var pref: Pref
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    constructor()
    constructor(mCtx: Context, list: List<OrderModel>) {
        this.mCtx = mCtx
        this.item = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_order, parent, false)
        val ViewHolder = ViewHolder(view)
        return ViewHolder
    }

    override fun getItemCount(): Int {
        return item.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: OrderModel = item.get(position)
        fauth = FirebaseAuth.getInstance()
        holder.total.text = model.total
        holder.jumlah.text = model.jumlah
        FirebaseDatabase.getInstance()
            .getReference("store/")
            .child(model.idstore!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(data2: DataSnapshot) {
                    val data =
                        data2.getValue(StoreModel::class.java)
                    model.storeModel = data
                    holder.namastore.text = model.storeModel!!.storename
                }

                override fun onCancelled(holder: DatabaseError) {
                    Log.e("cok", holder.message)
                }
            })
        FirebaseDatabase.getInstance()
            .getReference("store/")
            .child(model.idstore!!).child("listbarang/${model.idbarang}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(data2: DataSnapshot) {
                    val data =
                        data2.getValue(ListBarangModel::class.java)
                    model.barangModel = data
                    holder.namabarang.text = model.barangModel!!.namabarang
                    Glide.with(mCtx).load(model.barangModel!!.imagebarang)
                        .centerCrop().into(holder.imagebarang)
                }

                override fun onCancelled(holder: DatabaseError) {
                    Log.e("cok", holder.message)
                }
            })
        if (model.pembayaran.toString() == "Transfer BCA"){
            if (model.statuspembayaran.toString() == "n"){
                holder.status.text = "Menunggu Pembayaran"
            }else if (model.statuspembayaran.toString() == "y"){
                when {
                    model.statusbarang.toString() == "n" -> holder.status.text = "Pesanan Dibatalkan"
                    model.statusbarang.toString() == "p" -> holder.status.text = "Menunggu Konfirmasi Pengiriman"
                    model.statusbarang.toString() == "y" -> holder.status.text = "Pesanan Sedang Dikirim"
                    model.statusbarang.toString() == "s" -> holder.status.text = "Pesanan Sudah Diterima"
                }
            }
        } else if (model.pembayaran.toString() == "Bayar Di Tempat"){
            when {
                model.statusbarang.toString() == "n" -> holder.status.text = "Pesanan Dibatalkan"
                model.statusbarang.toString() == "p" -> holder.status.text = "Menunggu Konfirmasi Pengiriman"
                model.statusbarang.toString() == "y" -> holder.status.text = "Pesanan Sedang Dikirim"
                model.statusbarang.toString() == "s" -> holder.status.text = "Pesanan Sudah Diterima"
            }
        }
        holder.rl.setOnClickListener {
            val intent = Intent(mCtx, DetailOrder::class.java)
            intent.putExtra("idorder", model.idorder)
            mCtx.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rl: RelativeLayout
        var namastore: TextView
        var namabarang: TextView
        var jumlah: TextView
        var total: TextView
        var status: TextView
        var imagebarang: ImageView

        init {
            rl = itemView.findViewById(R.id.rel)
            namastore = itemView.findViewById(R.id.storename_order)
            namabarang = itemView.findViewById(R.id.nama_barang_order)
            total = itemView.findViewById(R.id.total_order)
            jumlah = itemView.findViewById(R.id.jumlah_order)
            status = itemView.findViewById(R.id.status_order)
            imagebarang = itemView.findViewById(R.id.image_barang_order)
        }
    }

}