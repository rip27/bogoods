package com.example.bogoods.page.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.example.bogoods.page.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.edit_profile_seller.*
import kotlinx.android.synthetic.main.profile_seller.*

class ProfileSeller : AppCompatActivity() {

    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_seller)
        setSupportActionBar(toolbar_profile)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pref = Pref(this)
        fAuth = FirebaseAuth.getInstance()
        val uid = fAuth.currentUser?.uid
        val job = "seller"

        FirebaseDatabase.getInstance().getReference("seller/$uid")
            .child("profile").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Glide.with(this@ProfileSeller)
                            .load(p0.value.toString())
                            .error(R.drawable.ic_seller)
                            .into(foto_profile)
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("$job/$uid")
            .child("name").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        name_on_profile.text = p0.value.toString()
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("$job/$uid")
            .child("email").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        email_on_profile.text = p0.value.toString()
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("$job/$uid")
            .child("phone").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        phone_on_profile.text = p0.value.toString()
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("$job/$uid")
            .child("gender").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        gender_on_profile.text = p0.value.toString()
                    }

                }
            )

        bt_logout.setOnClickListener {
            fAuth.signOut()
            pref.setStatusS(false)
            startActivity(Intent(this@ProfileSeller, SplashScreen::class.java))
            finish()
        }

        bt_edit.setOnClickListener {
            startActivity(Intent(this@ProfileSeller, EditProfileSeller::class.java))
        }
    }
}
