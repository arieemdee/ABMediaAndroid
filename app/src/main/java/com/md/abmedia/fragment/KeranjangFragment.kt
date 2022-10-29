package com.md.abmedia.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.md.abmedia.R
import com.md.abmedia.activity.MasukActivity
import com.md.abmedia.activity.PengirimanActivity
import com.md.abmedia.adapter.AdapterKeranjang
import com.md.abmedia.databinding.FragmentHomeBinding
import com.md.abmedia.databinding.FragmentKeranjangBinding
import com.md.abmedia.helper.Helper
import com.md.abmedia.helper.SharedPref
import com.md.abmedia.model.Produk
import com.md.abmedia.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class KeranjangFragment : Fragment() {
    private lateinit var _myDb: MyDatabase
    private lateinit var _s:SharedPref

    private var _binding: FragmentKeranjangBinding?=null
    private val _bdg get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeranjangBinding.inflate(inflater,container,false)
        //val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        _myDb = MyDatabase.getInstance(requireActivity())!!
        _s = SharedPref(requireActivity())
        mainButton()
        return _bdg.root // view
    }

    private lateinit var _adapter : AdapterKeranjang
    private var _listProduk = ArrayList<Produk>()
    private fun displayProduk(){
        _listProduk = _myDb.daoKeranjang().getAll() as ArrayList // kita paksa jadi array list

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        _adapter = AdapterKeranjang(requireActivity(),_listProduk,object:AdapterKeranjang.Listeners{
            override fun onUpdate() {
                //Log.d("onUpdate","call this")
                hitungTotal()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDelete(position: Int) {
                //Log.d("onDelete","call this")
                _listProduk.removeAt(position)
                _adapter.notifyDataSetChanged()
                hitungTotal()
                //Log.d("onDelete",adapter.itemCount.toString() )
                if(_adapter.itemCount==0){
                    _bdg.tvTotal.text = "Rp0"
                    _bdg.cbAll.isChecked = false
                }
            }

        }) //AdapterProduk(requireActivity(),listProduk)
        _bdg.rvProduk.adapter = _adapter
        _bdg.rvProduk.layoutManager = layoutManager
    }
    private var _totHrg = 0
    fun hitungTotal(){
        val listProduk = _myDb.daoKeranjang().getAll() as ArrayList // kita paksa jadi array list
        _totHrg = 0
        var isSelectedAll = true
        for(produk in listProduk){
            if(produk.selected){
                val hrg = Integer.valueOf(produk.harga)
                _totHrg +=(hrg*produk.jumlah)
            }else{
                isSelectedAll = false
            }
        }
        _bdg.cbAll.isChecked = isSelectedAll
        _bdg.tvTotal.text = Helper().rupiah(_totHrg)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun mainButton() {
        _bdg.btnDelete.setOnClickListener {
            val listDelete = ArrayList<Produk>()
            for (p in _listProduk){
                if(p.selected) listDelete.add(p)
            }
            delete(listDelete)
        }
        _bdg.btnBayar.setOnClickListener {

            if(_s.getStatusLogin()){
                var isThereProduct = false
                for(p in _listProduk){
                    if(p.selected) isThereProduct = true
                }

                if (isThereProduct){
                    val intent = Intent(requireActivity(),PengirimanActivity::class.java)
                    intent.putExtra("extra",_totHrg.toString())
                    startActivity(intent)
                }else {
                    Helper().toastSort(requireActivity(),"Tidak Ada Produk yg Terpilih")
                }
            }else{
                startActivity(Intent(requireActivity(),MasukActivity::class.java))
            }
        }
        _bdg.cbAll.setOnClickListener {
            for (i in _listProduk.indices){
                val produk = _listProduk[i]
                produk.selected = _bdg.cbAll.isChecked
                _listProduk[i] = produk
            }
            _adapter.notifyDataSetChanged()
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun delete(data:ArrayList<Produk>){
        CompositeDisposable().add(Observable.fromCallable { _myDb.daoKeranjang().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                //listener.onDelete()
                _listProduk.clear()
                _listProduk.addAll(_myDb.daoKeranjang().getAll() as ArrayList)
                _adapter.notifyDataSetChanged()
            })
    }
    /*
        lateinit var _btnDelete : ImageView
        lateinit var _rvProduk: RecyclerView
        lateinit var _tvTotal:TextView
        lateinit var _btnBayar:TextView
        lateinit var _cbAll:CheckBox
        private fun init(view: View) {
        _btnDelete = view.findViewById(R.id.btn_delete)
        _rvProduk = view.findViewById(R.id.rv_produk)
        _tvTotal = view.findViewById(R.id.tv_total)
        _btnBayar = view.findViewById(R.id.btn_bayar)
        _cbAll = view.findViewById(R.id.cb_all)
    }
    */

    override fun onResume() {
        displayProduk()
        hitungTotal()
        super.onResume()
    }

//    override fun onPause() {
//        super.onPause()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }

}