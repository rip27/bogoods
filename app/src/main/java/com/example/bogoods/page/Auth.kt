package com.example.bogoods.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bogoods.R
import com.example.bogoods.adapter.AuthAdapter
import kotlinx.android.synthetic.main.auth.*

class Auth : AppCompatActivity() {

    private var authAdapter: AuthAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth)

//        BACK TO CHOOSE AS
        ic_back_on_auth.setOnClickListener {
            onBackPressed()
        }

//        GET STRING JOB
        val job = intent.getStringExtra("job")

//        SHOW JOB
        if (job == "seller"){
            tv_job.text = "I'm Seller"
        }else{
            tv_job.text = "I'm Reseller"
        }

        val fragmentAdapter = AuthAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter

        tabLayout.setupWithViewPager(viewPager)

    }
}
