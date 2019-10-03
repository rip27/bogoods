package com.example.bogoods.adapter

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
import com.example.bogoods.model.ListBarangModel
import com.example.bogoods.model.StoreModel
import com.example.bogoods.model.UserModel
import com.example.bogoods.page.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class ListBarangAdapter : RecyclerView.Adapter<ListBarangAdapter.ListBarangViewHolder> {
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
            .inflate(R.layout.item_list_barang, parent, false)
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
        holder.harga.text = "Rp. " + lbModel.harga
        holder.stok.text = lbModel.stok
        Glide.with(mCtx).load(lbModel.imagebarang)
            .centerCrop()
            .error(R.drawable.ic_seller)
            .into(holder.imagelb)
        holder.ic_ed.setOnClickListener {
            val intent = Intent(mCtx, EditBarang::class.java)
            intent.putExtra("idstore", lbModel.idstore)
            intent.putExtra("idbarang", lbModel.idbarang)
            mCtx.startActivity(intent)
        }
        holder.ll.setOnClickListener {
            val intent = Intent(mCtx, DetailListBarang::class.java)
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
        var ic_ed: TextView

        init {
            ll = itemView.findViewById(R.id.ll_list_barang)
            namabarang = itemView.findViewById(R.id.tv_nama_barang)
            imagelb = itemView.findViewById(R.id.imagelistbarang)
            ic_ed = itemView.findViewById(R.id.ic_edit_barang)
            harga = itemView.findViewById(R.id.tv_harga_barang)
            stok = itemView.findViewById(R.id.tv_slot)
        }
    }
}