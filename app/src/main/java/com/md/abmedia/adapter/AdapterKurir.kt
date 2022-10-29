package com.md.abmedia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.md.abmedia.R
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.Alamat
import com.md.abmedia.model.rajaongkir.*
import java.util.*
import kotlin.collections.ArrayList

class AdapterKurir(var data: ArrayList<Costs>, var kurir: String, var listener: Listeners): RecyclerView.Adapter<AdapterKurir.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvLamaPengiriman = view.findViewById<TextView>(R.id.tv_lamaPengiriman)
        val tvBerat = view.findViewById<TextView>(R.id.tv_berat)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val layout = view.findViewById<LinearLayout>(R.id.layout)
        val rd = view.findViewById<RadioButton>(R.id.rd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kurir, parent, false)
        return  Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        holder.rd.isChecked = a.isActive
        holder.tvNama.text = kurir + " " + a.service
        val cost = a.cost[0]
        holder.tvLamaPengiriman.text = cost.etd + " Hari Kerja"
        holder.tvHarga.text = Helper().rupiah(cost.value)
        holder.tvBerat.text = "1 kg x "  + Helper().rupiah(cost.value)
        //holder.tvAlamat.text = "${a.alamat}, ${a.kota}, ${a.provinsi}, ${a.kodepos} , (${a.type})"
//
        holder.rd.setOnClickListener {
            a.isActive = true
            listener.onClicked(a,holder.adapterPosition)
        }
//
//        holder.layout.setOnClickListener {
//            a.isSelected = true
//            listener.onClicked(a)
//        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface Listeners{
        fun onClicked(data: Costs,index:Int)
    }
}