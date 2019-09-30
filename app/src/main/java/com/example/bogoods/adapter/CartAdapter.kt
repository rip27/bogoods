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
import com.example.bogoods.model.CartModel
import com.example.bogoods.model.ListBarangModel
import com.example.bogoods.model.StoreModel
import com.example.bogoods.model.UserModel
import com.example.bogoods.page.EditStore
import com.example.bogoods.page.ListBarang
import com.example.bogoods.page.ListRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder> {
    lateinit var mCtx: Context
    lateinit var itemStore: List<CartModel>
    lateinit var pref: Pref
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    constructor()
    constructor(mCtx: Context, list: List<CartModel>) {
        this.mCtx = mCtx
        this.itemStore = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_cart, parent, false)
        val ViewHolder = ViewHolder(view)
        return ViewHolder
    }

    override fun getItemCount(): Int {
        return itemStore.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: CartModel = itemStore.get(position)
        fauth = FirebaseAuth.getInstance()
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
                    holder.harga_per_item.text = "Rp. " + model.barangModel!!.harga
                    Glide.with(mCtx).load(model.barangModel!!.imagebarang)
                        .centerCrop()
                        .error(R.drawable.ic_seller)
                        .into(holder.image)
                    holder.stok.text = model.jumlah
                    holder.subtotal.text = "Rp. " + (model.jumlah.toString().toInt() * model.barangModel!!.harga.toString().toInt()).toString()
                }

                override fun onCancelled(holder: DatabaseError) {
                    Log.e("cok", holder.message)
                }
            })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rl: RelativeLayout
        var namastore: TextView
        var namabarang: TextView
        var stok: TextView
        var harga_per_item: TextView
        var subtotal: TextView
        var image: ImageView

        init {
            rl = itemView.findViewById(R.id.rel_)
            namastore = itemView.findViewById(R.id.storename_cart)
            image = itemView.findViewById(R.id.imagebarang_cart)
            namabarang = itemView.findViewById(R.id.item_name)
            harga_per_item = itemView.findViewById(R.id.harga_per_item)
            subtotal = itemView.findViewById(R.id.subtotal)
            stok = itemView.findViewById(R.id.stok_cart)
        }
    }
}