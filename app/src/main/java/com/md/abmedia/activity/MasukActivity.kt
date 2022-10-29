package com.md.abmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.md.abmedia.R
import com.md.abmedia.helper.SharedPref

class MasukActivity : AppCompatActivity() {
    lateinit var s:SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)
        s = SharedPref(this)

        mainButton()
    }

    private fun mainButton(){
        val btnLogin = findViewById<Button>(R.id.btn_prosesLogin)
        btnLogin.setOnClickListener{
            //s.setStatusLogin(true)
            startActivity(Intent(this,LoginActivity::class.java))
        }
        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}