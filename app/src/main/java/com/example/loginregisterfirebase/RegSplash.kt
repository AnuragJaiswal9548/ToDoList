package com.example.loginregisterfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class RegSplash : AppCompatActivity() {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_splash)
        
        handler= Handler()
        handler.postDelayed({
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}