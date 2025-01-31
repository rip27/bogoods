package com.example.bogoods.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bogoods.R
import com.example.bogoods.data.Pref
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.choose_as.*

class ChooseAs : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_as)
        pref = Pref(this)
//        WHEN CLICKED IMAGE RESELLER
        im_reseller.setOnClickListener {
            val intent = Intent(this@ChooseAs, FillData::class.java)
//            PUT STRING JOB
            intent.putExtra("job", "reseller")
            startActivity(intent)
        }

//        WHEN CLICKED IMAGE SELLER
        im_seller.setOnClickListener {
            val intent = Intent(this@ChooseAs, FillData::class.java)
//            PUT STRING JOB
            intent.putExtra("job", "seller")
            startActivity(intent)
        }

//        SESSION AUTH


    }
}
