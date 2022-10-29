package com.md.abmedia.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.md.abmedia.R
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.Produk
import com.md.abmedia.room.MyDatabase
import com.md.abmedia.util.Config
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class AdapterKeranjang(var activity: Activity, var data: ArrayList<Produk>,var listener:Listeners): RecyclerView.Adapter<AdapterKeranjang.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk_keranjang)
        val layout = view.findViewById<CardView>(R.id.layout)

        val btnTambah = view.findViewById<ImageView>(R.id.btn_tambah)
        val btnKurang = view.findViewById<ImageView>(R.id.btn_kurang)
        val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return  Holder(view)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        var produk = data[position]
        var harga = Integer.valueOf(produk.harga)
        holder.tvNama.text = produk.name
        holder.tvHarga.text = Helper().rupiah(harga * produk.jumlah)

        var jumlah = produk.jumlah
        holder.tvJumlah.text = jumlah.toString()

        holder.checkBox.isChecked = produk.selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            produk.selected = isChecked
            update(produk)
        }

        //holder.imgProduk.setImageResource(data[position].image)
        val image = Config.productUrl+ data[position].image
        //Log.d("urlImageMD",image)
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.product) // jika terjadi error kasi gambar default
            .error(R.drawable.product)//jika link ga ada
            .into(holder.imgProduk)

//        holder.layout.setOnClickListener {
//            val act = Intent(activity, DetailProdukActivity::class.java)
//            val str = Gson().toJson(data[position],Produk::class.java)
//            act.putExtra("extra",str)
//            activity.startActivity(act)
//        }

        holder.btnTambah.setOnClickListener {
            jumlah++
            produk.jumlah = jumlah
            update(produk)
            holder.tvJumlah.text = jumlah.toString()
            holder.tvHarga.text = Helper().rupiah (harga * jumlah)
        }
        holder.btnKurang.setOnClickListener {
            if(jumlah <= 1) return@setOnClickListener
            jumlah--
            produk.jumlah = jumlah
            update(produk)
            holder.tvJumlah.text = jumlah.toString()
            holder.tvHarga.text = Helper().rupiah (harga * jumlah)
        }
        holder.btnDelete.setOnClickListener {
            delete(produk)
            listener.onDelete(position)
        }
    }

    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)
    }

    private fun update(data:Produk){
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }
    private fun delete(data:Produk){
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                //listener.onDelete()
            })
    }
    override fun getItemCount(): Int {
        return data.size
    }
}