package com.example.bogoods.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bogoods.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

//        ACTION SPLASH SCREEN
        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, Login::class.java))
            finish()
        }, 1000)

    }
}
