package com.md.abmedia.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.md.abmedia.R
import com.md.abmedia.databinding.ActivityDetailProdukBinding
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.Produk
import com.md.abmedia.room.MyDatabase
import com.md.abmedia.util.Config
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailProdukActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityDetailProdukBinding
    private lateinit var _myDb: MyDatabase
    private lateinit var _produk: Produk
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailProdukBinding.inflate(layoutInflater)
        _myDb = MyDatabase.getInstance(this)!! // call database
        val view = _binding.root
        setContentView(view)
        //setContentView(R.layout.activity_detail_produk)
        getInfo()
        mainButton()
        checkKeranjang()
    }
    private fun mainButton(){
        _binding.btnKeranjang.setOnClickListener{
            val data = _myDb.daoKeranjang().getProduk(_produk.id)
            if(data == null){
                insert()
            } else {
                data.jumlah = data.jumlah + 1
                update(data)
            }
        }
        _binding.btnFavorit.setOnClickListener {
            val listNote = _myDb.daoKeranjang().getAll() // get All data
            for(note :Produk in listNote){
                println("-----------------------")
                println(note.name)
                println(note.harga)
            }
        }
        _binding.includeToolbar.btnToKeranjang.setOnClickListener {
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }
    private fun insert(){

        //val myDb: MyDatabase = MyDatabase.getInstance(this)!! // call database
        //val note = Produk() //create new keranjang
        //note.name = "First keranjang"
        //note.harga = "9000"
                                  //import io.reactivex.Observable
        CompositeDisposable().add(Observable.fromCallable { _myDb.daoKeranjang().insert(_produk) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkKeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this,"Berhasil ditambah kekeranjang",Toast.LENGTH_SHORT).show()
            })
    }
    private fun update(data:Produk){

        //val myDb: MyDatabase = MyDatabase.getInstance(this)!! // call database
        //val note = Produk() //create new keranjang
        //note.name = "First keranjang"
        //note.harga = "9000"
        //import io.reactivex.Observable
        CompositeDisposable().add(Observable.fromCallable { _myDb.daoKeranjang().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkKeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this,"Berhasil ditambah kekeranjang",Toast.LENGTH_SHORT)
            })
    }
    private fun checkKeranjang(){
        val dataKeranjang = _myDb.daoKeranjang().getAll()
        if(dataKeranjang.isNotEmpty()){
            _binding.includeToolbar.divAngka.visibility = View.VISIBLE
            _binding.includeToolbar.tvAngka.text = dataKeranjang.size.toString()
        } else {
            _binding.includeToolbar.divAngka.visibility = View.GONE
        }
    }
    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        _produk = Gson().fromJson<Produk>(data,Produk::class.java)

        _binding.tvNama.text = _produk.name
        _binding.tvHarga.text = Helper().rupiah(_produk.harga)
        _binding.tvDeskripsi.text = _produk.deskripsi

        //holder.imgProduk.setImageResource(data[position].image)
        val link = Config.productUrl+ _produk.image
        Picasso.get()
            .load(link)
            .placeholder(R.drawable.product) // jika terjadi error kasi gambar default
            .error(R.drawable.product)//jika link ga ada
            .resize(400,400)
            .into(_binding.image)

        //set Toolbar
        //val toolbar = findViewById<Toolbar>(R.id.toolbar_custom)
        //setSupportActionBar(binding.includeToolbar.toolbarCustom)
        Helper().setToolbar(this, _binding.includeToolbar.toolbarCustom,_produk.name)
        //supportActionBar!!.title = produk.name
        //supportActionBar!!.setDisplayShowHomeEnabled(true)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}