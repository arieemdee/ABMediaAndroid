package com.md.abmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.md.abmedia.adapter.AdapterRiwayat
import com.md.abmedia.app.ApiConfig

import com.md.abmedia.databinding.ActivityRiwayatBinding
import com.md.abmedia.helper.Helper
import com.md.abmedia.helper.SharedPref
import com.md.abmedia.model.ResponModel
import com.md.abmedia.model.Transaksi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityRiwayatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRiwayatBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)
        //setContentView(R.layout.activity_baru)
        Helper().setToolbar(this, _binding.includeTb.toolbar, "Riwayat Belanja")
    }

    fun getRiwayat() {
        val id = SharedPref(this).getUser()!!.id
        ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    displayRiwayat(res.transaksis)
                }
            }
        })
    }

    fun displayRiwayat(transaksis: ArrayList<Transaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        _binding.rvRiwayat.adapter = AdapterRiwayat(transaksis, object : AdapterRiwayat.Listeners {
            override fun onClicked(data: Transaksi) {
                val json = Gson().toJson(data, Transaksi::class.java)
                val intent = Intent(this@RiwayatActivity, DetailTransaksiActivity::class.java)
                intent.putExtra("transaksi", json)
                startActivity(intent)
            }
        })
        _binding.rvRiwayat.layoutManager = layoutManager
    }

    override fun onResume() {
        getRiwayat()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        //onBackPressed()
        return super.onSupportNavigateUp()
    }
}