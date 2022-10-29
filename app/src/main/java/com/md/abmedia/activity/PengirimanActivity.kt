package com.md.abmedia.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.md.abmedia.R
import com.md.abmedia.adapter.AdapterKurir
import com.md.abmedia.app.ApiConfigAlamat
import com.md.abmedia.databinding.ActivityPengirimanBinding
import com.md.abmedia.helper.Helper
import com.md.abmedia.helper.SharedPref
import com.md.abmedia.model.CheckOut
import com.md.abmedia.model.rajaongkir.ResponOngkir
import com.md.abmedia.model.rajaongkir.*
import com.md.abmedia.room.MyDatabase
import com.md.abmedia.util.ApiKey
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengirimanActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityPengirimanBinding
    private lateinit var _myDb : MyDatabase

    var _totHrg = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPengirimanBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)
        Helper().setToolbar(this, _binding.includedTb.toolbar,"Pengiriman")
        //setContentView(R.layout.activity_pengiriman)
        _myDb = MyDatabase.getInstance(this)!!

        _totHrg = Integer.valueOf(intent.getStringExtra("extra")!!)
        _binding.tvTotalBelanja.text = Helper().rupiah(_totHrg)

        mainButton()
        setSpinner()
    }

    private fun setSpinner() {
        val arryString = ArrayList<String>()
        arryString.add("JNE")
        arryString.add("POS")
        arryString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this, R.layout.item_spinner,arryString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        _binding.spnKurir.adapter = adapter

        _binding.spnKurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position !=0){
                    getOngkir(_binding.spnKurir.selectedItem.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    @SuppressLint("SetTextI18n")
    fun cekAlamat(){

        if(_myDb.daoAlamat().getByStatus(true)!=null){
            _binding.divAlamat.visibility = View.VISIBLE
            _binding.divKosong.visibility = View.GONE

            _binding.divMetodePengiriman.visibility = View.VISIBLE

            val a = _myDb.daoAlamat().getByStatus(true)!!
            _binding.tvNama.text = a.name
            _binding.tvPhone.text = a.phone
            _binding.tvAlamat.text = "${a.alamat}, ${a.kota }, ${a.provinsi}, ${a.kodepos} , (${a.type})"
            _binding.btnTambahAlamat.text = "Ubah Alamat"

            getOngkir("JNE")
        }else{
            _binding.divAlamat.visibility = View.GONE
            _binding.divKosong.visibility = View.VISIBLE
            _binding.btnTambahAlamat.text = "Tambah Alamat"
        }
    }

    private fun mainButton() {
        _binding.btnTambahAlamat.setOnClickListener {
            startActivity(Intent(this,ListAlamatActivity::class.java))
        }
        _binding.btnBayar.setOnClickListener {
            bayar()
        }
    }

    private fun bayar() {
        val user = SharedPref(this).getUser()
        val a = _myDb.daoAlamat().getByStatus(true)!!

        val listProduk = _myDb.daoKeranjang().getAll() as ArrayList // kita paksa jadi array list
        var totItem = 0
        var totHrg = 0
        val produks = ArrayList<CheckOut.Item>()
        for (p in listProduk){
            if(p.selected){
                totItem+=p.jumlah
                totHrg +=(p.jumlah * Integer.valueOf(p.harga))

                val prd = CheckOut.Item()
                prd.id = p.id.toString()
                prd.total_item = p.jumlah.toString()
                prd.total_harga = (p.jumlah * Integer.valueOf(p.harga)).toString()
                prd.catatan = "Catatan baru"
                produks.add(prd)
            }
        }

        val checkOut  = CheckOut()
        checkOut.user_id= user!!.id.toString()
        checkOut.total_item = totItem.toString()
        checkOut.total_harga = totHrg.toString()
        checkOut.name = a.name
        checkOut.phone = a.phone
        checkOut.jasa_pengiriman = _jasaKirim
        checkOut.ongkir = _ongkir
        checkOut.kurir = _kurir
        checkOut.detail_lokasi = _binding.tvAlamat.text.toString()
        Log.d("cek tot","$totHrg = $_ongkir")
        checkOut.total_transfer = "" + (totHrg + Integer.valueOf(_ongkir))
        checkOut.produks = produks

        val json = Gson().toJson(checkOut,CheckOut::class.java)
        Log.d("Respon:","checkOut_json: "+json)
        val intent = Intent(this,PembayaranActivity::class.java)
        intent.putExtra("extra",json)
        startActivity(intent)
    }

    private fun getOngkir(kurir: String){
        val alamat = _myDb.daoAlamat().getByStatus(true)

        val origin = "501"
        val destination = alamat!!.id_kota.toString()
        val berat = 1000

        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key,origin,destination,berat,kurir.lowercase()).enqueue(object :
            Callback<ResponOngkir> {

            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {
                if(response.isSuccessful){
                    Log.d("GET ONGKIR","Berhasil Memuat Data")
                    val result = response.body()!!.rajaongkir.results
                    if(result.isNotEmpty()){
                        displayOngkir(result[0].code.uppercase() ,result[0].costs)
                    }

                }else {
                    Log.d("GET ONGKIR Else","Gagal memuat data: "+response.message())
                }

            }

            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {
                Log.d("ONGKIR ERR ","Gagal memuat data: "+ t.message)
                //Toast.makeText(this@LoginActivity,"Error"+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    var _ongkir = ""
    var _kurir = ""
    var _jasaKirim = ""
    private fun displayOngkir(kurir: String, arrList: ArrayList<Costs>){
        var arrOngkir = ArrayList<Costs>()
        //membuat agar yg gerceklist pertakali itu yg index awal
        for (i in arrList.indices){
            val ongkir = arrList[i]
            if(i==0){
                ongkir.isActive = true
            }
            arrOngkir.add(ongkir)
        }

        setTotal(arrOngkir[0].cost[0].value)
        _ongkir = arrOngkir[0].cost[0].value
        _kurir = kurir
        _jasaKirim = arrOngkir[0].service

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var adapter: AdapterKurir? = null
        adapter = AdapterKurir(arrOngkir,kurir, object: AdapterKurir.Listeners{
            @SuppressLint("NotifyDataSetChanged")
            override fun onClicked(data: Costs, index: Int) {
                //ini untuk membuat unceklist yg lain ketika salah satu di ceklist
                val newArrOngkir = ArrayList<Costs>()
                for(ongkir in arrOngkir ){
                    ongkir.isActive = data.description == ongkir.description
                    newArrOngkir.add(ongkir)
                }
                arrOngkir = newArrOngkir
                adapter!!.notifyDataSetChanged()
                setTotal(data.cost[0].value)
                _ongkir = data.cost[0].value
                _kurir = kurir
                _jasaKirim = data.service
            }
        })
        _binding.rvMetode.adapter = adapter
        _binding.rvMetode.layoutManager = layoutManager
    }

    fun setTotal(ongkir: String){
        _binding.tvOngkir.text = Helper().rupiah(ongkir)
        _binding.tvTotal.text = Helper().rupiah(Integer.valueOf(ongkir) + _totHrg)
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        cekAlamat()
        super.onResume()
    }
}