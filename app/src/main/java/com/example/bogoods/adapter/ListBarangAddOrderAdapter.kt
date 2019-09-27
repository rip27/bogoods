package com.example.bogoods.adapter

import android.content.Context
import android.content.Intent
import com.example.bogoods.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bogoods.data.Pref
import com.example.bogoods.model.ListBarangModel
import com.example.bogoods.page.DetailBarang
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListBarangAddOrderAdapter : RecyclerView.Adapter<ListBarangAddOrderAdapter.ListBarangViewHolder> {
    lateinit var mCtx: Context
    lateinit var itemList: List<ListBarangModel>
    lateinit var pref: Pref
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    constructor()
    constructor(mCtx: Context, list: List<ListBarangModel>) {
        this.mCtx = mCtx
        this.itemList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBarangViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_barang_add_order, parent, false)
        val lsbViewHolder = ListBarangViewHolder(view)
        return lsbViewHolder
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ListBarangViewHolder, position: Int) {
        val lbModel: ListBarangModel = itemList.get(position)
        fauth = FirebaseAuth.getInstance()
        holder.namabarang.text = lbModel.namabarang
        holder.harga.text = lbModel.harga
        holder.stok.text = lbModel.stok
        Glide.with(mCtx).load(lbModel.imagebarang)
            .centerCrop()
            .error(R.drawable.ic_seller)
            .into(holder.imagelb)
        holder.ll.setOnClickListener {
            val intent = Intent(mCtx, DetailBarang::class.java)
            intent.putExtra("idstore", lbModel.idstore)
            intent.putExtra("idbarang", lbModel.idbarang)
            mCtx.startActivity(intent)
        }
    }

    inner class ListBarangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var namabarang: TextView
        var harga: TextView
        var stok: TextView
        var imagelb: ImageView

        init {
            ll = itemView.findViewById(R.id.ll_list_barang_add_order)
            namabarang = itemView.findViewById(R.id.tv_nama_barang_add_order)
            imagelb = itemView.findViewById(R.id.imagelistbarang_add_order)
            harga = itemView.findViewById(R.id.tv_harga_barang_add_order)
            stok = itemView.findViewById(R.id.tv_slot_add_order)
        }
    }
}