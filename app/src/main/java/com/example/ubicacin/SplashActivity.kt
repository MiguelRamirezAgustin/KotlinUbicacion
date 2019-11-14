package com.example.ubicacin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity(),Runnable {

    private lateinit var  handler:Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        simulaCarga()
    }

    private fun simulaCarga(){
        handler = Handler()
        handler.postDelayed(this@SplashActivity, 3000)
    }

    override fun run() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
