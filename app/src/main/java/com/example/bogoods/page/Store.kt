package com.example.bogoods.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bogoods.R
import kotlinx.android.synthetic.main.store.*

class Store : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store)
        setSupportActionBar(toolbar_store)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
