package com.md.abmedia.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.md.abmedia.R
import com.md.abmedia.adapter.AdapterProdukTransaksi
import com.md.abmedia.app.ApiConfig
import com.md.abmedia.databinding.ActivityDetailTransaksiBinding
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.DetailTransaksi
import com.md.abmedia.model.Produk
import com.md.abmedia.model.ResponModel
import com.md.abmedia.model.Transaksi
import com.md.abmedia.room.MyDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class DetailTransaksiActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityDetailTransaksiBinding
    private lateinit var _myDb: MyDatabase
    private lateinit var _produk: Produk
    var _transaksi = Transaksi()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailTransaksiBinding.inflate(layoutInflater)
        _myDb = MyDatabase.getInstance(this)!! // call database
        val view = _binding.root
        setContentView(view)
        //setContentView(R.layout.activity_detail_produk)
        Helper().setToolbar(this, _binding.includeTb.toolbar, "Detail Transaksi")

        val json = intent.getStringExtra("transaksi")
        _transaksi = Gson().fromJson(json, Transaksi::class.java)

        setData(_transaksi)
        displayProduk(_transaksi.details)
        mainButton()
    }
    private fun mainButton() {
        _binding.btnBatal.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Transaksi akan di batalkan dan tidak bisa di kembalikan!")
                .setConfirmText("Yes, Batalkan")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                      batalTransaksi()
                }
                .setCancelText("Tutup")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }.show()
        }

        _binding.btnBayar.setOnClickListener {
//            ImagePicker.with(this)
//                .crop()
//                .maxResultSize(512,512)
//                .createIntentFromDialog { launcher.launch(it) }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                // Use the uri to load the image
                Log.d("TAG IMG", uri.toString())
            }
        }

    fun batalTransaksi() {
        val loading = SweetAlertDialog(this@DetailTransaksiActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()
        ApiConfig.instanceRetrofit.batalCheckout(_transaksi.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1) {
                    val sweetAlert = SweetAlertDialog(this@DetailTransaksiActivity, SweetAlertDialog.SUCCESS_TYPE)
                    sweetAlert.titleText = "Success..."
                    sweetAlert.contentText = "Transaksi berhasil dibatalakan"
                    sweetAlert.hideConfirmButton()
                    sweetAlert.setConfirmClickListener {
                        it.dismissWithAnimation()
                        finish()
                    }
                    sweetAlert.show()

//                    Timer("SettingUp", false).schedule(3000) {
//                        sweetAlert.dismiss()
//                    }

                      Helper().toastSort(this@DetailTransaksiActivity,"Transaksi Berhasil dibatalkan")
                      finish() //onBackPressed()
//                    displayRiwayat(res.transaksis)
                } else {
                    error(res.message)
                }
            }
        })
    }

    fun error(pesan: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(pesan)
            .show()
    }

    @SuppressLint("SetTextI18n")
    fun setData(t: Transaksi) {
        _binding.tvStatus.text = t.status

        val formatBaru = "dd MMMM yyyy, kk:mm:ss"
        _binding.tvTgl.text = Helper().convertTanggal(t.created_at, formatBaru)

        _binding.tvPenerima.text = t.name + " - " + t.phone
        _binding.tvAlamat.text = t.detail_lokasi
        _binding.tvKodeUnik.text = Helper().rupiah(t.kode_unik)
        _binding.tvTotalBelanja.text = Helper().rupiah(t.total_harga)
        _binding.tvOngkir.text = Helper().rupiah(t.ongkir)
        _binding.tvTotal.text = Helper().rupiah(t.total_transfer)

        if (t.status != "MENUNGGU") _binding.divFooter.visibility = View.GONE

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            var color = getColor(R.color.menungu)
            if (t.status == "SELESAI") color = getColor(R.color.selesai)
            else if (t.status == "BATAL") color = getColor(R.color.batal)
            _binding.tvStatus.setTextColor(color)
        }else{
            var color = ContextCompat.getColor(this, R.color.menungu);
            if (t.status == "SELESAI") color = ContextCompat.getColor(this,R.color.selesai)
            else if (t.status == "BATAL") color = ContextCompat.getColor(this,R.color.batal)
            _binding.tvStatus.setTextColor(color)
        }

    }

    fun displayProduk(transaksis: ArrayList<DetailTransaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        _binding.rvProduk.adapter = AdapterProdukTransaksi(transaksis)
        _binding.rvProduk.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }
}