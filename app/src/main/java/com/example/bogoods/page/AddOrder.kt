package com.example.bogoods.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bogoods.R
import kotlinx.android.synthetic.main.add_order.*

class AddOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_order)
        setSupportActionBar(toolbar_add_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
