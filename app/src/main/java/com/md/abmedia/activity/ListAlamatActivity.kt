package com.md.abmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.md.abmedia.adapter.AdapterAlamat
import com.md.abmedia.databinding.ActivityListAlamatBinding
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.Alamat
import com.md.abmedia.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListAlamatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityListAlamatBinding
    lateinit var myDb: MyDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAlamatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_list_alamat)
        Helper().setToolbar(this,binding.includeTb.toolbar,"Pilih Alamat" )
        myDb = MyDatabase.getInstance(this)!!
        mainButton()
    }

    private fun displayAlamat(){
        //val myDb = MyDatabase.getInstance(this)!!
        val arrList = myDb.daoAlamat().getAll() as ArrayList
        if (arrList.isEmpty()) binding.divKosong.visibility = View.VISIBLE
        else binding.divKosong.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvAlamat.adapter = AdapterAlamat(arrList, object: AdapterAlamat.Listeners{
            override fun onClicked(data: Alamat) {
                if(myDb.daoAlamat().getByStatus(true)!=null) {
                    val alamatAktive = myDb.daoAlamat().getByStatus(true)!!
                    alamatAktive.isSelected = false
                    updateActive(alamatAktive,data)
                }
            }
        })
        binding.rvAlamat.layoutManager = layoutManager
    }

    private fun updateActive(dataActive: Alamat,dataNonActive: Alamat){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(dataActive) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                updateNonActive(dataNonActive)
            })
    }
    private fun updateNonActive(data: Alamat){
        data.isSelected = true
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                finish()
            })
    }
    override fun onResume() {
        displayAlamat()
        super.onResume()
    }

    private fun mainButton() {
        binding.btnTambahAlamat.setOnClickListener {
            startActivity(Intent(this,TambahAlamatActivity::class.java))
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }

}