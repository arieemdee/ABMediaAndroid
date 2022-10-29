package com.md.abmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.md.abmedia.MainActivity
import com.md.abmedia.R
import com.md.abmedia.app.ApiConfig
import com.md.abmedia.helper.SharedPref
import com.md.abmedia.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var _s:SharedPref
    lateinit var _fcm:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        _s = SharedPref(this)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        getFcm()
        btnLogin.setOnClickListener{
            login()
        }

    }

    fun getFcm(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Respon", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            _fcm = token.toString()
            // Log and toast
            Log.d("Respon fcm:", _fcm)
        })
    }

    fun login(){
        val edtEmail = findViewById<EditText>(R.id.edt_email)
        val edtPassword = findViewById<EditText>(R.id.edt_password)
        val pb = findViewById<ProgressBar>(R.id.pb)

        if(edtEmail.text.isEmpty()){
            edtEmail.error = "Kolom Email tdk boleh kosong"
            edtEmail.requestFocus()
            return
        }else if(edtPassword.text.isEmpty()){
            edtPassword.error = "Kolom Password tdk boleh kosong"
            edtPassword.requestFocus()
            return
        }
        pb.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.login(edtEmail.text.toString(),edtPassword.text.toString(),_fcm).enqueue(object : Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb.visibility = View.GONE
                val respon = response.body()!!
                if(respon.success == 1){
                    _s.setStatusLogin(true)
                    _s.setUser(respon.user)
//                    s.setString(s.nama,respon.user.name)
//                    s.setString(s.phone,respon.user.phone)
//                    s.setString(s.email,respon.user.email)

                    val inten = Intent(this@LoginActivity,MainActivity::class.java)
                    inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) // akan menghapus semua activity yg ada sebelum'y
                    startActivity(inten)
                    finish()
                    Toast.makeText(this@LoginActivity ,"Welcome ${respon.user.name}", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@LoginActivity,"Error ${respon.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb.visibility = View.GONE
                Toast.makeText(this@LoginActivity,"Error"+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}