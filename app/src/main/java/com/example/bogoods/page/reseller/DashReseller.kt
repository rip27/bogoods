package com.example.bogoods.page.reseller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.example.bogoods.page.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.dash.*

class DashReseller : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var uid = firebaseAuth.currentUser?.uid
    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dash)

        pref = Pref(this)

        FirebaseDatabase.getInstance().getReference("reseller/$uid/job").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    jobb.text = p0.value.toString()
                }

            }
        )
        FirebaseDatabase.getInstance().getReference("reseller/$uid/name").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    name.text = p0.value.toString()
                }

            }
        )

        logout.setOnClickListener {
            firebaseAuth.signOut()
            pref.setStatusR(false)
            startActivity(Intent(this@DashReseller, SplashScreen::class.java))
            finish()
        }

    }
}
