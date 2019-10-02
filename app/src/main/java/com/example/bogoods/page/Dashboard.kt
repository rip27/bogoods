package com.example.bogoods.page

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_dashboard.*
import kotlinx.android.synthetic.main.nav_header_dashboard.*
import java.util.*

class Dashboard : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_dashboard)


        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)
//
//        val db = FirebaseDatabase.getInstance().getReference("cart")
//        for (i in 0..1000) {
//            db.child(UUID.randomUUID().toString()).setValue(UUID.randomUUID().toString())
//        }

        FirebaseDatabase.getInstance().getReference("user/${fAuth.currentUser?.uid}")
            .child("name").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    val name = p0.value.toString()
                    welcomename.text = "Welcome " + name
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        FirebaseDatabase.getInstance().getReference("user/${fAuth.currentUser?.uid}")
            .child("job").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    val jbb = p0.value.toString()
                    welcomejob.text = jbb
                    if (jbb == "seller") {
                        add_order_dashboard.visibility = View.GONE
                        add_connection_store_dashboard.visibility = View.GONE
                        your_order_dashboard.visibility = View.GONE
                        my_store.visibility = View.VISIBLE
                        accept_order_dashboard.visibility = View.VISIBLE
                        accept_req_connection_store_dashboard.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        goto_profile.setOnClickListener {
            startActivity(Intent(this@Dashboard, Profile::class.java))
        }

        add_order_dashboard.setOnClickListener {
            startActivity(Intent(this@Dashboard, AddOrder::class.java))
        }

        my_store.setOnClickListener {
            startActivity(Intent(this@Dashboard, MyStore::class.java))
        }

        accept_req_connection_store_dashboard.setOnClickListener {
            startActivity(Intent(this@Dashboard, AcceptRequest::class.java))
        }

        accept_order_dashboard.setOnClickListener {
            startActivity(Intent(this@Dashboard, AccOrder::class.java))
        }

        add_connection_store_dashboard.setOnClickListener {
            startActivity(Intent(this@Dashboard, ReqConStore::class.java))
        }

        your_order_dashboard.setOnClickListener {
            startActivity(Intent(this@Dashboard, YourOrder::class.java))
        }

    }
}
