package com.example.bogoods.page.seller

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import com.bumptech.glide.Glide
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.nav_header_dashboard_seller.*

class DashboardSeller : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_seller)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

        val fab: FloatingActionButton = findViewById(R.id.add_store)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        FirebaseDatabase.getInstance().getReference("seller/${fAuth.currentUser?.uid}")
            .child("profile").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    Glide.with(this@DashboardSeller).load(p0.value.toString())
                        .centerCrop()
                        .error(R.drawable.ic_seller)
                        .into(foto_profil_dashboard)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })


        FirebaseDatabase.getInstance().getReference("seller/${fAuth.currentUser?.uid}")
            .child("name").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    nama_dashboard.text = p0.value.toString()
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        FirebaseDatabase.getInstance().getReference("seller/${fAuth.currentUser?.uid}")
            .child("email").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    email_dashboard.text = p0.value.toString()
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })


        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard_seller, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this@DashboardSeller, ProfileSeller::class.java)
                intent.putExtra("job", "seller")
                startActivity(intent)
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
