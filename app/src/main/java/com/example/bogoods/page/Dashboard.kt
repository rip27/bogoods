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

class Dashboard : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)


        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        val navView: NavigationView = findViewById(R.id.nav_view)
//        val toggle = ActionBarDrawerToggle(
//            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        FirebaseDatabase.getInstance().getReference("user/${fAuth.currentUser?.uid}")
//            .child("profile").addListenerForSingleValueEvent(object : ValueEventListener {
//
//                override fun onDataChange(p0: DataSnapshot) {
//                    Glide.with(this@Dashboard).load(p0.value.toString())
//                        .centerCrop()
//                        .error(R.drawable.ic_seller)
//                        .into(foto_profil_dashboard)
//                }
//
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//            })
//

        FirebaseDatabase.getInstance().getReference("user/${fAuth.currentUser?.uid}")
            .child("name").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    val name = p0.value.toString()
                    nama_dashboard.text = name
                    welcomename.text = "Welcome " + name
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        FirebaseDatabase.getInstance().getReference("user/${fAuth.currentUser?.uid}")
            .child("job").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    val jbb = p0.value.toString()
                    tv_job_on_dashboard.text = jbb
                    welcomejob.text = jbb
                    if (jbb == "seller"){
                        add_order_dashboard.visibility = View.GONE
                        add_connection_store_dashboard.visibility = View.GONE
                        my_store.visibility = View.VISIBLE
                        accept_order_dashboard.visibility = View.VISIBLE
                        accept_req_connection_store_dashboard.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
//        FirebaseDatabase.getInstance().getReference("user/${fAuth.currentUser?.uid}")
//            .child("email").addListenerForSingleValueEvent(object : ValueEventListener {
//
//                override fun onDataChange(p0: DataSnapshot) {
//                    email_dashboard.text = p0.value.toString()
//                }
//
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//            })

//
//        navView.setNavigationItemSelectedListener(this)

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

        }

        add_connection_store_dashboard.setOnClickListener {
            startActivity(Intent(this@Dashboard, ReqConStore::class.java))
        }

    }

//    override fun onBackPressed() {
//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.dashboard, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        val jb = intent.getStringExtra("job")
//        when (item.itemId) {
//            R.id.nav_profile -> {
//                val intent = Intent(this@Dashboard, Profile::class.java)
//                intent.putExtra("job", jb)
//                startActivity(intent)
//            }
//            R.id.nav_store -> {
//                val intent = Intent(this@Dashboard, MyStore::class.java)
//                intent.putExtra("job", jb)
//                startActivity(intent)
//            }
//            R.id.nav_your_order -> {
//                val intent = Intent(this@Dashboard, YourOrder::class.java)
//                intent.putExtra("job", jb)
//                startActivity(intent)
//            }
//            R.id.nav_tools -> {
//
//            }
//        }
//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        drawerLayout.closeDrawer(GravityCompat.START)
//        return true
//    }
}
