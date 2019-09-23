package com.example.bogoods.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.text.Editable
import android.text.TextWatcher
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
import com.example.bogoods.page.ListBarang
import com.example.bogoods.page.ListRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class AccStoreAdapter : RecyclerView.Adapter<AccStoreAdapter.AccstoreViewHolder> {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccstoreViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_accept_store, parent, false)
        val viewHolder = AccstoreViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return itemStore.size
    }

    override fun onBindViewHolder(holder: AccstoreViewHolder, position: Int) {
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
        holder.ll.setOnClickListener {
            val intent = Intent(mCtx, ListRequest::class.java)
            intent.putExtra("idstore", storeModel.idstore)
            mCtx.startActivity(intent)
        }
    }


    inner class AccstoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var namastore: TextView
        var pemilik: TextView
        var address: TextView
        var count: TextView
        var imagestore: ImageView

        init {
            ll = itemView.findViewById(R.id.ll_store_ar)
            namastore = itemView.findViewById(R.id.tv_nama_store_ar)
            imagestore = itemView.findViewById(R.id.imagestorear)
            pemilik = itemView.findViewById(R.id.tv_pemilik_storear)
            address = itemView.findViewById(R.id.tv_addressar)
            count = itemView.findViewById(R.id.count_req)
        }
    }
}