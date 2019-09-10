package com.example.bogoods.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.register.*

class RegisterFragment : Fragment() {

    lateinit var pref: Pref
    lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fAuth = FirebaseAuth.getInstance()
        pref = Pref(context!!)
        bt_register.setOnClickListener {
            bt_register.visibility = View.GONE
            progressRegist.visibility = View.VISIBLE
            val name = et_nama_register.text.toString()
            val phone = et_phone_register.text.toString()
            val gender = sp_gender_register.selectedItem.toString()
            val email = et_email_register.text.toString()
            val password = et_password_register.text.toString()
            if (name.isEmpty() || phone.isEmpty() || gender.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Fill All Data", Toast.LENGTH_SHORT).show()
                bt_register.visibility = View.VISIBLE
                progressRegist.visibility = View.GONE
            } else {
                fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            addUserToFirebase(name, phone, gender, email, password)
                            Toast.makeText(context, "Register Berhasil!", Toast.LENGTH_SHORT).show()
                            bt_register.visibility = View.VISIBLE
                            progressRegist.visibility = View.GONE
                        } else {
                            Toast.makeText(context, "GAGAL", Toast.LENGTH_SHORT).show()
                            bt_register.visibility = View.VISIBLE
                            progressRegist.visibility = View.GONE
                        }
                    }
            }
        }
    }

    private fun addUserToFirebase(name: String, phone: String, gender: String, email: String, password: String) {
        val uid = fAuth.currentUser?.uid
        val job = activity!!.intent.getStringExtra("job")
        dbRef = FirebaseDatabase.getInstance().getReference("$job/$uid")
        dbRef.child("/id").setValue(uid)
        dbRef.child("/name").setValue(name)
        dbRef.child("/phone").setValue(phone)
        dbRef.child("/gender").setValue(gender)
        dbRef.child("/email").setValue(email)
        dbRef.child("/job").setValue(job)
        dbRef.child("/password").setValue(password)
    }

    companion object {
        fun newInstance(): RegisterFragment {
            val fragment = RegisterFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
