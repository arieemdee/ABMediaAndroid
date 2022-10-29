package com.md.abmedia.adapter
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.md.abmedia.R
import com.md.abmedia.activity.DetailProdukActivity
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.Produk
import com.md.abmedia.util.Config
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
class AdapterProduk(var activity: Activity, var data: ArrayList<Produk>): RecyclerView.Adapter<AdapterProduk.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvHargaAsli = view.findViewById<TextView>(R.id.tv_hargaAsli)
        val imgProduk = view.findViewById<ImageView>(R.id.img_Produk)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return  Holder(view)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val a = data[position]
        val hrgAsli = Integer.valueOf(a.harga)
        var hrg = Integer.valueOf(a.harga)

        if(a.discount != 0){
            hrg-= a.discount
        }

        holder.tvHargaAsli.text = Helper().rupiah(hrgAsli)
        //Memberi efek coret hargany
        holder.tvHargaAsli.paintFlags = holder.tvHargaAsli.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = Helper().rupiah(hrg)
        //holder.imgProduk.setImageResource(data[position].image)
        val image = Config.productUrl + data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.product) // jika terjadi error kasi gambar default
            .error(R.drawable.product)//jika link ga ada
            .into(holder.imgProduk)

        holder.layout.setOnClickListener {
            val act = Intent(activity, DetailProdukActivity::class.java)

            val str = Gson().toJson(data[position],Produk::class.java)
            act.putExtra("extra",str)

            activity.startActivity(act)
        }
    }
    override fun getItemCount(): Int {
        return data.size
    }
}