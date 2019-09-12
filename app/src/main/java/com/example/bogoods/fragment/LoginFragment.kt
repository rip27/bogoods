package com.example.bogoods.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.example.bogoods.page.Dashboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.login.*

class LoginFragment : Fragment() {

    lateinit var pref: Pref
    lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = Pref(context!!)
        fAuth = FirebaseAuth.getInstance()
        val job = activity!!.intent.getStringExtra("job")
        bt_login.setOnClickListener {
            bt_login.visibility = View.GONE
            progressLogin.visibility = View.VISIBLE
            val email = et_email_login.text.toString()
            val password = et_password_login.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Fill All Data", Toast.LENGTH_SHORT).show()
                bt_login.visibility = View.VISIBLE
                progressLogin.visibility = View.GONE
            } else {
                fAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("$job")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    val user = fAuth.currentUser
                                    if (job == "seller") {
                                        pref.setStatusS(true)
                                        updateUI(user)
                                    } else {
                                        pref.setStatusR(true)
                                        updateUI(user)
                                    }
                                }

                            })
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Username atau Password salah!",
                            Toast.LENGTH_SHORT
                        ).show()
                        bt_login.visibility = View.VISIBLE
                        progressLogin.visibility = View.GONE
                    }
            }
        }
    }

    fun updateUIseller(user: FirebaseUser?) {
        if (user != null) {
            pref.saveUID(user.uid)
            startActivity(Intent(context, Dashboard::class.java))
        } else {
            Log.e("TAG_ERROR", "user tidak ada")
        }
    }
    fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            pref.saveUID(user.uid)
            startActivity(Intent(context, Dashboard::class.java))
        } else {
            Log.e("TAG_ERROR", "user tidak ada")
        }
    }

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
