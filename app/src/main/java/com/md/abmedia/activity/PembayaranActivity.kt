package com.md.abmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.md.abmedia.R
import com.md.abmedia.adapter.AdapterBank
import com.md.abmedia.adapter.AdapterProduk
import com.md.abmedia.app.ApiConfig
import com.md.abmedia.databinding.ActivityPembayaranBinding
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.Bank
import com.md.abmedia.model.CheckOut
import com.md.abmedia.model.ResponModel
import com.md.abmedia.model.Transaksi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PembayaranActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityPembayaranBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPembayaranBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)
        Helper().setToolbar(this,_binding.includeTb.toolbar,"Pembayaran")
        //setContentView(R.layout.activity_pembayaran)
        displayBank()
    }
    fun displayBank(){
        val arrBank = ArrayList<Bank>()
        arrBank.add(Bank("BCA","112233445566","Bejo1", R.drawable.logo_bca))
        arrBank.add(Bank("BRI","112233445567","Bejo2",R.drawable.logo_bri))
        arrBank.add(Bank("MANDIRI","112233445568","Bejo3",R.drawable.logo_madiri))
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        _binding.rvData.layoutManager = layoutManager
        _binding.rvData.adapter = AdapterBank(arrBank,object :AdapterBank.Listeners{
            override fun onClicked(data: Bank, index: Int) {
                bayar(data)
            }
        })

    }

    private fun bayar(bank:Bank){
        val json = intent.getStringExtra("extra")!!.toString()
        val chekout = Gson().fromJson(json,CheckOut::class.java)
        chekout.bank = bank.nama

        val loading = SweetAlertDialog(this@PembayaranActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()

        ApiConfig.instanceRetrofit.checkout(chekout).enqueue(object : Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                if(!response.isSuccessful){
                    error(response.message())
                    return
                }
                val respon = response.body()!!
                //Log.d("resbody",respon.toString())
                if(respon.success == 1){
                    //Helper().toastSort(this@PembayaranActivity,"Berhasil Kirim Ke sever")
                    val jsBank = Gson().toJson(bank,Bank::class.java)
                    val jsTrx = Gson().toJson(respon.transaksi,Transaksi::class.java)
                    val jsCheckOut = Gson().toJson(chekout,CheckOut::class.java)
                    val intent = Intent(this@PembayaranActivity,SuccessActivity::class.java)
                    intent.putExtra("bank",jsBank)
                    intent.putExtra("transaksi",jsTrx)
                    intent.putExtra("checkout",jsCheckOut)
                    startActivity(intent)
                }else{
                    error("Gagal Kirim ke Server")
                    //Helper().toastSort(this@PembayaranActivity,"Gagal Kirim Ke sever")
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                //Helper().toastSort(this@PembayaranActivity,"Error"+t.message)
                error(t.message.toString())
            }

        })
    }
    fun error(pesan: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(pesan)
            .show()
    }
    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }
}