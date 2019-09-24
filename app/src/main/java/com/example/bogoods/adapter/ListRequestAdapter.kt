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
import com.example.bogoods.model.ListRequestModel
import com.example.bogoods.model.StoreModel
import com.example.bogoods.model.UserModel
import com.example.bogoods.page.ListBarang
import com.example.bogoods.page.ListRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ListRequestAdapter : RecyclerView.Adapter<ListRequestAdapter.lrViewHolder> {
    lateinit var mCtx: Context
    lateinit var itemReq: List<ListRequestModel>
    lateinit var pref: Pref
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    constructor()
    constructor(mCtx: Context, list: List<ListRequestModel>) {
        this.mCtx = mCtx
        this.itemReq = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): lrViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_request, parent, false)
        val viewHolder = lrViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return itemReq.size
    }

    override fun onBindViewHolder(holder: lrViewHolder, position: Int) {
        val listRequestModel: ListRequestModel = itemReq.get(position)
        fauth = FirebaseAuth.getInstance()
        FirebaseDatabase.getInstance()
            .getReference("user/")
            .child(listRequestModel.iduserrequest!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(data2: DataSnapshot) {
                    val userData =
                        data2.getValue(UserModel::class.java)
                    listRequestModel.userModel = userData
                    holder.namareq.text = listRequestModel.userModel!!.name
                    Glide.with(mCtx).load(listRequestModel.userModel!!.profile)
                        .centerCrop()
                        .error(R.drawable.ic_seller)
                        .into(holder.profile)
                }

                override fun onCancelled(holder: DatabaseError) {
                    Log.e("cok", holder.message)
                }
            })
        if (listRequestModel.status == "accept" || listRequestModel.status == "reject"){
            holder.acc.visibility = View.GONE
            holder.rj.visibility = View.GONE
            holder.dl.visibility = View.VISIBLE
        }

        holder.dl.setOnClickListener {
            var dialog: android.app.AlertDialog
            val alertDialog = android.app.AlertDialog.Builder(mCtx)
            alertDialog.setTitle("DELETE RESELLER")
            alertDialog.setPositiveButton("YES") { dialog, i ->
                dbRef = FirebaseDatabase.getInstance().getReference("store/${listRequestModel.idstore}/requestconnection")
                    dbRef.child(listRequestModel.key!!).removeValue()
                Toast.makeText(
                    mCtx,
                    "Sukses Delete",
                    Toast.LENGTH_SHORT
                ).show()
            }
            alertDialog.setNegativeButton("NO") { dialog, i ->
                dialog.dismiss()
            }
            dialog = alertDialog.create()
            dialog.show()
        }

        holder.acc.setOnClickListener {
            dbRef = FirebaseDatabase.getInstance().reference
            dbRef.child("store")
                .child("/${listRequestModel.idstore}")
                .child("/requestconnection")
                .child("/${listRequestModel.idreq}/status").setValue("accept")
            Toast.makeText(mCtx, "Text Acc", Toast.LENGTH_SHORT).show()
        }
        holder.rj.setOnClickListener {
            dbRef = FirebaseDatabase.getInstance().reference
            dbRef.child("store")
                .child("/${listRequestModel.idstore}")
                .child("/requestconnection")
                .child("/${listRequestModel.idreq}/status").setValue("reject")
            Toast.makeText(mCtx, "Text Rej", Toast.LENGTH_SHORT).show()
        }
    }


    inner class lrViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: RelativeLayout
        var acc: LinearLayout
        var rj: LinearLayout
        var dl: LinearLayout
        var namareq: TextView
        var profile: CircleImageView

        init {
            ll = itemView.findViewById(R.id.ll_req)
            namareq = itemView.findViewById(R.id.name_request)
            profile = itemView.findViewById(R.id.profile_list_request)
            acc = itemView.findViewById(R.id.accept_request)
            rj = itemView.findViewById(R.id.reject_request)
            dl = itemView.findViewById(R.id.delete_from_reseller)
        }
    }
}