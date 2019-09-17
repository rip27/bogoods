package com.example.bogoods.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.login.*

class Login : AppCompatActivity() {

    lateinit var pref: Pref
    lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        pref = Pref(this)
        fAuth = FirebaseAuth.getInstance()
        val job = intent.getStringExtra("job")
        bt_login.setOnClickListener {
            bt_login.visibility = View.GONE
            progressLogin.visibility = View.VISIBLE
            val email = et_email_login.text.toString()
            val password = et_password_login.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@Login, "Fill All Data", Toast.LENGTH_SHORT).show()
                bt_login.visibility = View.VISIBLE
                progressLogin.visibility = View.GONE
            } else {
                fAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("user")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    val user = fAuth.currentUser
                                    if (job == "seller") {
                                        pref.setStatusS(true)
                                        updateUI(user, job)
                                    } else if (job == "reseller"){
                                        pref.setStatusR(true)
                                        updateUI(user, job)
                                    }
                                }

                            })
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@Login,
                            "Username atau Password salah!",
                            Toast.LENGTH_SHORT
                        ).show()
                        bt_login.visibility = View.VISIBLE
                        progressLogin.visibility = View.GONE
                    }
            }
        }
    }

    fun updateUI(user: FirebaseUser?, job: String) {
        if (user != null) {
            pref.saveUID(user.uid)
            val intent = Intent(this@Login, Dashboard::class.java)
            intent.putExtra("job", job)
            startActivity(intent)
        } else {
            Log.e("TAG_ERROR", "user tidak ada")
        }
    }
}
