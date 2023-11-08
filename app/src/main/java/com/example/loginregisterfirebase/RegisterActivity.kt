package com.example.loginregisterfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title = "Register"

        auth = FirebaseAuth.getInstance()

        var editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        var editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        var buttonLogin = findViewById<Button>(R.id.buttonRegister)

        buttonLogin.setOnClickListener {
            var email1 = editTextEmailAddress.text.toString()
            var pwd1: String = editTextPassword.text.toString()

            register(email1, pwd1)
        }
    }
    fun register(email1: String, pwd1:String){
        val email=email1
        val password=pwd1

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this,RegSplash::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG)
                .show()
        }
    }

    fun goToLogin(view: View){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

}