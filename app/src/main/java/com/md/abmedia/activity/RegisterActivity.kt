package com.md.abmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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

class RegisterActivity : AppCompatActivity() {
    lateinit var _s: SharedPref
    lateinit var _fcm:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        _s = SharedPref(this)
        getFcm()
        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener {
            register()
        }
        val btnDummy = findViewById<RelativeLayout>(R.id.btn_google)
        btnDummy.setOnClickListener {
            dataDummy()
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
    private fun dataDummy() {
        val edtNama = findViewById<EditText>(R.id.edt_nama)
        val edtEmail = findViewById<EditText>(R.id.edt_email)
        val edtPhone = findViewById<EditText>(R.id.edt_phone)
        val edtPassword = findViewById<EditText>(R.id.edt_password)
        edtNama.setText("syafiq")
        edtEmail.setText("admin3@gmail.com")
        edtPhone.setText("08562501188")
        edtPassword.setText("admin112233")
    }

    fun register(){
        val edtNama = findViewById<EditText>(R.id.edt_nama)
        val edtEmail = findViewById<EditText>(R.id.edt_email)
        val edtPhone = findViewById<EditText>(R.id.edt_phone)
        val edtPassword = findViewById<EditText>(R.id.edt_password)
        val pb = findViewById<ProgressBar>(R.id.pb)

        if(edtNama.text.isEmpty()){
            edtNama.error = "Kolom Nama tdk boleh kosong"
            edtNama.requestFocus()
            return
        }else if(edtEmail.text.isEmpty()){
            edtEmail.error = "Kolom Email tdk boleh kosong"
            edtEmail.requestFocus()
            return
        }else if(edtPhone.text.isEmpty()){
            edtPhone.error = "Kolom HP tdk boleh kosong"
            edtPhone.requestFocus()
            return
        }else if(edtPassword.text.isEmpty()){
            edtPassword.error = "Kolom Password tdk boleh kosong"
            edtPassword.requestFocus()
            return
        }

        pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(edtNama.text.toString(),edtEmail.text.toString(),edtPhone.text.toString(),edtPassword.text.toString(),_fcm).enqueue(object : Callback<ResponModel>{

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb.visibility = View.GONE
                var respon = response.body()!!
                if(respon.success == 1){
                    _s.setStatusLogin(true)
                    val inten = Intent(this@RegisterActivity, MainActivity::class.java)
                    inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) // akan menghapus semua activity yg ada sebelum'y
                    startActivity(inten)
                    finish()
                    Toast.makeText(this@RegisterActivity,"Welcome ${respon.user.name}",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@RegisterActivity,"Error ${respon.message}",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb.visibility = View.GONE
                Toast.makeText(this@RegisterActivity,"Error"+t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
}