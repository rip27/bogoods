package com.example.bogoods.adapter

import android.annotation.SuppressLint
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
import com.example.bogoods.model.RatingModel
import com.example.bogoods.model.StoreModel
import com.example.bogoods.model.UserModel
import com.example.bogoods.page.DetailBarang
import com.example.bogoods.page.DetailPesanan
import com.example.bogoods.page.ListBarang
import com.example.bogoods.page.ListRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RatingAdapter : RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    lateinit var mCtx: Context
    lateinit var item: List<RatingModel>
    lateinit var pref: Pref
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    constructor()
    constructor(mCtx: Context, list: List<RatingModel>) {
        this.mCtx = mCtx
        this.item = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_rating, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return item.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: RatingModel = item.get(position)
        fauth = FirebaseAuth.getInstance()
        holder.coment.text = "Comment : \n ${model.comment}"
        holder.rating.text = "Rating ${model.rating.toString()}"
        FirebaseDatabase.getInstance()
            .getReference("user/")
            .child(model.idrating!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(data2: DataSnapshot) {
                    val userData =
                        data2.getValue(UserModel::class.java)
                    model.userModel = userData
                    holder.nama.text = "Ulasan oleh ${model.userModel!!.name}"
                }

                override fun onCancelled(holder: DatabaseError) {
                    Log.e("cok", holder.message)
                }
            })
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var nama: TextView
        var rating: TextView
        var coment: TextView

        init {
            ll = itemView.findViewById(R.id.lllll)
            nama = itemView.findViewById(R.id.nama_rating)
            rating  = itemView.findViewById(R.id.i_rating)
            coment = itemView.findViewById(R.id.i_comment)
        }
    }


}