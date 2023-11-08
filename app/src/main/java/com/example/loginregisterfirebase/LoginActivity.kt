package com.example.loginregisterfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.util.*
import kotlin.math.sqrt


class LoginActivity : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth

    //sensors
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    //sensor close


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title="Login"
        auth= FirebaseAuth.getInstance()
        var editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        var editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        var buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
           var email1 =  editTextEmailAddress.text.toString()
           var pwd1:String =  editTextPassword.text.toString()

            login(email1,pwd1)
        }


        //sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        //sensor

    }
    fun login(email1: String, pwd1:String){
        val email=email1
        val password= pwd1
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG)
                .show()
        }
    }

    fun goToRegister(view: View){
        val intent= Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }


    // sensor
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {
//                Toast.makeText(applicationContext, "Shake event detected", Toast.LENGTH_SHORT).show()
                val intent=Intent(this@LoginActivity,RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }
    //sensor

}