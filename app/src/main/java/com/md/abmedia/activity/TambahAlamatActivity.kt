package com.md.abmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.md.abmedia.R
import com.md.abmedia.app.ApiConfigAlamat
import com.md.abmedia.databinding.ActivityTambahAlamatBinding
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.Alamat
import com.md.abmedia.model.ModelAlamat
import com.md.abmedia.model.ResponModel
import com.md.abmedia.room.MyDatabase
import com.md.abmedia.util.ApiKey
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahAlamatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTambahAlamatBinding
    var provinsi = ModelAlamat.Provinsi()
    var kota = ModelAlamat.Provinsi()
    var kecamatan = ModelAlamat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahAlamatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Helper().setToolbar(this,binding.includeTb.toolbar,"Tambah Alamat")
        //setContentView(R.layout.activity_tambah_alamat)
        mainButton()
        getProvisi()
    }

    private fun mainButton() {
        binding.btnSimpan.setOnClickListener {
            simpan()
        }
    }

    private fun simpan(){
        when {
            binding.edtNama.text.isEmpty() -> {
                binding.edtNama.error = "Kolom Nama tdk boleh kosong"
                binding.edtNama.requestFocus()
                return
            }
            binding.edtType.text.isEmpty() -> {
                binding.edtType.error = "Kolom Type tdk boleh kosong"
                binding.edtType.requestFocus()
                return
            }
            binding.edtPhone.text.isEmpty() -> {
                binding.edtPhone.error = "Kolom HP tdk boleh kosong"
                binding.edtPhone.requestFocus()
                return
            }
            binding.edtAlamat.text.isEmpty() -> {
                binding.edtAlamat.error = "Kolom Alamat tdk boleh kosong"
                binding.edtAlamat.requestFocus()
                return
            }
            binding.edtKodePos.text.isEmpty() -> {
                binding.edtKodePos.error = "Kolom KodePos tdk boleh kosong"
                binding.edtKodePos.requestFocus()
                return
            }
            provinsi.province_id == "0" -> {
                Helper().toastSort(this,"Silahkan Pilih Provinsi")
                return
            }
            kota.city_id == "0" -> {
                Helper().toastSort(this,"Silahkan Pilih Kota")
                return
            }
            //kecamatan.id==0 -> {
            //    Helper().toastSort(this,"Silahkan Pilih Kecamatan")
            //    return
            //}
        }

        val alamat = Alamat()
        alamat.name = binding.edtNama.text.toString()
        alamat.type = binding.edtType.text.toString()
        alamat.phone = binding.edtPhone.text.toString()
        alamat.alamat = binding.edtAlamat.text.toString()
        alamat.kodepos = binding.edtKodePos.text.toString()
        alamat.id_provinsi = Integer.valueOf(provinsi.province_id)
        alamat.provinsi = provinsi.province

        alamat.id_kota = Integer.valueOf(kota.city_id)
        alamat.kota = kota.city_name

        //alamat.id_kecamatan = kecamatan.id
        //alamat.kecamatan = kecamatan.nama

        insert(alamat)

    }

    private fun getProvisi(){
        ApiConfigAlamat.instanceRetrofit.getPropinsi(ApiKey.key).enqueue(object :
            Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                if(response.isSuccessful){
                    binding.pb.visibility = View.GONE
                    binding.divProvinsi.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Provinsi")

                    val listProvinsi = res.rajaongkir.results

                    for (prov in listProvinsi){
                        arryString.add(prov.province)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity,R.layout.item_spinner,arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnProvinsi.adapter = adapter

                    binding.spnProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                            if(position !=0){
                                provinsi = listProvinsi[position-1]
                                //Log.d("Propinsi", "" + listProvinsi[position-1].id + " " + listProvinsi[position-1].nama)
                                binding.pb.visibility = View.VISIBLE
                                getKota(provinsi.province_id)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }

                }else {
                    binding.pb.visibility = View.GONE
                    Log.d("ERROR","Gagal memuat data: "+response.message())
                }

            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                //pb.visibility = View.GONE
                //Toast.makeText(this@LoginActivity,"Error"+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun getKota(id:String){
        ApiConfigAlamat.instanceRetrofit.getKota(ApiKey.key,id).enqueue(object :
            Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                if(response.isSuccessful){
                    binding.pb.visibility = View.GONE
                    binding.divKota.visibility = View.VISIBLE
                    val res = response.body()!!
                    val listKab = res.rajaongkir.results

                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kota")
                    for (kota in listKab){
                        arryString.add("${kota.type} ${kota.city_name}")
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity,R.layout.item_spinner,arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnKota.adapter = adapter

                    binding.spnKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                            if(position !=0){
                                kota = listKab[position-1]
                                val kodePos = kota.postal_code
                                binding.edtKodePos.setText(kodePos)
                                //Log.d("Kabupaten", "" + listKab[position-1].id + " " + listKab[position-1].nama)
                                //binding.pb.visibility = View.VISIBLE
                                //getKecamatan(listKab[position-1].city_id)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
                }else {
                    binding.pb.visibility = View.GONE
                    Log.d("ERROR","Gagal memuat data: "+response.message())
                }

            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                //pb.visibility = View.GONE
                //Toast.makeText(this@LoginActivity,"Error"+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    /*
    private fun getKecamatan(id:Int){
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object :
            Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                if(response.isSuccessful){
                    binding.pb.visibility = View.GONE
                    binding.divKecamatan.visibility = View.VISIBLE
                    val arryString = ArrayList<String>()
                    val res = response.body()!!
                    val listKec = res.kecamatan
                    arryString.add("Pilih Kecamatan")
                    for (kec in listKec){
                        arryString.add(kec.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity,R.layout.item_spinner,arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnKecamatan.adapter = adapter

                    binding.spnKecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                            if(position !=0){
                                kecamatan = listKec[position-1]
                                //Log.d("Kabupaten", "" + listKab[position-1].id + " " + listKab[position-1].nama)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                    }

                }else {
                    binding.pb.visibility = View.GONE
                    Log.d("ERROR","Gagal memuat data: "+response.message())
                }

            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                //pb.visibility = View.GONE
                //Toast.makeText(this@LoginActivity,"Error"+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    */

    private fun insert(data:Alamat){

        //val myDb: MyDatabase = MyDatabase.getInstance(this)!! // call database
        //val note = Produk() //create new keranjang
        //note.name = "First keranjang"
        //note.harga = "9000"
        //import io.reactivex.Observable
        val myDb = MyDatabase.getInstance(this)!!
        //init beri value checked
        if(myDb.daoAlamat().getByStatus(true)==null) {
            data.isSelected = true
        }

        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().insert(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Helper().toastSort(this,"Insert data success")
                for(alamat in myDb.daoAlamat().getAll()){
                    Log.d("Alamat", "nama" + alamat.name + " - " + alamat.alamat)
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }
}