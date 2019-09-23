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
import com.example.bogoods.model.StoreModel
import com.example.bogoods.model.UserModel
import com.example.bogoods.page.ListBarang
import com.example.bogoods.page.ListRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.JointViewHolder> {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JointViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_store, parent, false)
        val jointViewHolder = JointViewHolder(view)
        return jointViewHolder
    }

    override fun getItemCount(): Int {
        return itemStore.size
    }

    override fun onBindViewHolder(holder: JointViewHolder, position: Int) {
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
            var dialog: android.app.AlertDialog
            val alertDialog = android.app.AlertDialog.Builder(mCtx)
            val view = LayoutInflater.from(mCtx).inflate(R.layout.popup_on_store, null)
            val list = view.findViewById<ListView>(R.id.store_popup)
            list.setOnItemClickListener { adapterView, view, i, l ->
                when(i){
                    0 -> {
                        val intent = Intent(mCtx, ListBarang::class.java)
                        intent.putExtra("idstore", storeModel.idstore)
                        mCtx.startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(mCtx, ListRequest::class.java)
                        intent.putExtra("idstore", storeModel.idstore)
                        mCtx.startActivity(intent)
                    }
                }
            }
            alertDialog.setView(view)
            alertDialog.setTitle(storeModel.storename)
            dialog = alertDialog.create()
            dialog.show()
        }
    }

    inner class JointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var namastore: TextView
        var pemilik: TextView
        var address: TextView
        var imagestore: ImageView

        init {
            ll = itemView.findViewById(R.id.ll_store)
            namastore = itemView.findViewById(R.id.tv_nama_store)
            imagestore = itemView.findViewById(R.id.imagestore)
            pemilik = itemView.findViewById(R.id.tv_pemilik_store)
            address = itemView.findViewById(R.id.tv_address)
        }
    }
}