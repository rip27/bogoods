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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ConStoreAdapter : RecyclerView.Adapter<ConStoreAdapter.ConstoreViewHolder> {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstoreViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_req_store, parent, false)
        val viewHolder = ConstoreViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return itemStore.size
    }

    override fun onBindViewHolder(holder: ConstoreViewHolder, position: Int) {
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
        holder.req.setOnClickListener {
            var dialog: android.app.AlertDialog
            val alertDialog = android.app.AlertDialog.Builder(mCtx)
            alertDialog.setTitle("REQUEST CONNECTION")
            alertDialog.setPositiveButton("REQUEST") { dialog, i ->
                val idreq = UUID.randomUUID().toString()
                dbRef = FirebaseDatabase.getInstance().getReference("store/${storeModel.idstore}/requestconnection/$idreq")
                dbRef.child("iduserrequest").setValue(fauth.currentUser?.uid)
                dbRef.child("idreq").setValue(idreq)
                dbRef.child("status").setValue("request")
                Toast.makeText(
                    mCtx,
                    "Sukses Request",
                    Toast.LENGTH_SHORT
                ).show()
            }
            alertDialog.setNegativeButton("NO") { dialog, i ->
                dialog.dismiss()
            }
            dialog = alertDialog.create()
            dialog.show()
        }
    }

    inner class ConstoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var namastore: TextView
        var pemilik: TextView
        var address: TextView
        var req: TextView
        var imagestore: ImageView

        init {
            ll = itemView.findViewById(R.id.ll_req_store)
            namastore = itemView.findViewById(R.id.tv_nama_store_req)
            imagestore = itemView.findViewById(R.id.image_req_store)
            pemilik = itemView.findViewById(R.id.tv_pemilik_store_req)
            address = itemView.findViewById(R.id.tv_address_req)
            req = itemView.findViewById(R.id.req_store)
        }
    }
}