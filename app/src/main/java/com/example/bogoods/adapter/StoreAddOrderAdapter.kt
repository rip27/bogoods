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
import com.example.bogoods.model.StoreModel
import com.example.bogoods.model.UserModel
import com.example.bogoods.page.EditStore
import com.example.bogoods.page.ListBarang
import com.example.bogoods.page.ListBarangAddOrder
import com.example.bogoods.page.ListRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class StoreAddOrderAdapter : RecyclerView.Adapter<StoreAddOrderAdapter.StoreViewHolder> {
    lateinit var mCtx: Context
    lateinit var itemStore: List<StoreModel>
    lateinit var pref: Pref
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    constructor()
    constructor(mCtx: Context, list: List<StoreModel>) {
        this.mCtx = mCtx
        this.itemStore = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_store_on_add_order, parent, false)
        val jointViewHolder = StoreViewHolder(view)
        return jointViewHolder
    }

    override fun getItemCount(): Int {
        return itemStore.size
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val storeModel: StoreModel = itemStore.get(position)
        fauth = FirebaseAuth.getInstance()
        holder.namastore.text = storeModel.storename
        holder.address.text = storeModel.address
        Glide.with(mCtx).load(storeModel.imagestore)
            .centerCrop()
            .error(R.drawable.ic_seller)
            .into(holder.imagestore)
        FirebaseDatabase.getInstance()
            .getReference("user/")
            .child(storeModel.idpemilik!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(data2: DataSnapshot) {
                    val userData =
                        data2.getValue(UserModel::class.java)
                    storeModel.usermodel = userData
                    holder.pemilik.text = storeModel.usermodel!!.name
                }

                override fun onCancelled(holder: DatabaseError) {
                    Log.e("cok", holder.message)
                }
            })
        FirebaseDatabase.getInstance().getReference("store/${storeModel.idstore}/listbarang")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    holder.count.text = p0.childrenCount.toString() + " Produk"
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        FirebaseDatabase.getInstance().getReference("store/${storeModel.idstore}/requestconnection")
            .orderByChild("status").equalTo("accept")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    holder.countr.text = p0.childrenCount.toString() + " Orang"
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        holder.ll.setOnClickListener {
            val intent = Intent(mCtx, ListBarangAddOrder::class.java)
            intent.putExtra("idstore", storeModel.idstore)
            mCtx.startActivity(intent)
        }
    }

    inner class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var namastore: TextView
        var pemilik: TextView
        var address: TextView
        var count: TextView
        var countr: TextView
        var imagestore: ImageView

        init {
            ll = itemView.findViewById(R.id.ll_store_on_add_order)
            namastore = itemView.findViewById(R.id.tv_nama_store_on_add_order)
            imagestore = itemView.findViewById(R.id.imagestore_on_add_order)
            pemilik = itemView.findViewById(R.id.tv_pemilik_store_on_add_order)
            address = itemView.findViewById(R.id.tv_address_on_add_order)
            count = itemView.findViewById(R.id.count_produk)
            countr = itemView.findViewById(R.id.count_reseller)
        }
    }
}