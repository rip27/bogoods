package com.example.bogoods.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.profile.*

class Profile : AppCompatActivity() {

    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        setSupportActionBar(toolbar_profile)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pref = Pref(this)
        fAuth = FirebaseAuth.getInstance()
        val uid = fAuth.currentUser?.uid
        val job = intent.getStringExtra("job")

        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("job").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        tv_job_on_profile.text = p0.value.toString()
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("profile").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Glide.with(this@Profile)
                            .load(p0.value.toString())
                            .error(R.drawable.ic_seller)
                            .into(foto_profile)
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("name").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        name_on_profile.text = p0.value.toString()
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("email").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        email_on_profile.text = p0.value.toString()
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("user/$uid")
            .child("phone").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        phone_on_profile.text = p0.value.toString()
                    }

                }
            )

        FirebaseDatabase.getInstance().getReference("user/$uid")
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
            pref.setStatusR(false)
            startActivity(Intent(this@Profile, SplashScreen::class.java))
            finish()
        }

        bt_edit.setOnClickListener {
            startActivity(Intent(this@Profile, EditProfile::class.java))
        }
    }
}
