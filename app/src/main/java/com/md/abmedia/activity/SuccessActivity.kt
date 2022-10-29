package com.md.abmedia.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.md.abmedia.MainActivity
import com.md.abmedia.databinding.ActivitySuccessBinding
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.Bank
import com.md.abmedia.model.CheckOut
import com.md.abmedia.model.Transaksi
import com.md.abmedia.room.MyDatabase

class SuccessActivity : AppCompatActivity() {
    private lateinit var _binding:ActivitySuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySuccessBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)
        Helper().setToolbar(this,_binding.includeTb.toolbar,"Bank Transfer")
        //setContentView(R.layout.activity_success)
        setValue()
        mainButton()
    }
    var _nominal = 0
    private fun mainButton() {
        _binding.btnCopyNoRek.setOnClickListener {
            copyText(_binding.tvNomorRekening.text.toString())
        }
        _binding.btnCopyNominal.setOnClickListener {
            copyText(_nominal.toString())
        }
        _binding.btnCekStatus.setOnClickListener {
            startActivity(Intent(this,RiwayatActivity::class.java))
        }
    }

    private fun copyText(text:String){
        val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyText = ClipData.newPlainText("text",text)
        copyManager.setPrimaryClip(copyText)
        Helper().toastSort(this,"Copy to CLIPBOARD Success")
    }

    private fun setValue() {
        val jsBank = intent.getStringExtra("bank")
        val jsTrx = intent.getStringExtra("transaksi")
        val jsCheckout = intent.getStringExtra("checkout")

        val bank = Gson().fromJson(jsBank, Bank::class.java)
        val trx =  Gson().fromJson(jsTrx, Transaksi::class.java)
        val checout = Gson().fromJson(jsCheckout,CheckOut::class.java)//get u/ keperluan hapus krn sdh di bayar

        //hapus keranjang
        val myDb = MyDatabase.getInstance(this)!!
        for(prd in checout.produks){
            myDb.daoKeranjang().deleteById(prd.id)
        }

        _binding.tvNomorRekening.text = bank.rekening
        _binding.tvNamaPenerima.text = bank.penerima
        _binding.imageBank.setImageResource(bank.image)
        _nominal = Integer.valueOf(trx.total_transfer) + Integer.valueOf(trx.kode_unik)
        _binding.tvNominal.text = Helper().rupiah(_nominal)

    }
    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {


        //inginnya ke back ke main activity tidak ke satu2
        val inten = Intent(this, MainActivity::class.java)
        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) // akan menghapus semua activity yg ada sebelum'y
        startActivity(inten)
        finish()
        super.onBackPressed()
    }
}