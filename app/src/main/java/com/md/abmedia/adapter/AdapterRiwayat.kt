package com.md.abmedia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.md.abmedia.R
import com.md.abmedia.helper.Helper
import com.md.abmedia.model.Transaksi
import java.util.*
import kotlin.collections.ArrayList

class AdapterRiwayat(var data: ArrayList<Transaksi>, var listener: Listeners): RecyclerView.Adapter<AdapterRiwayat.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvTangal = view.findViewById<TextView>(R.id.tv_tgl)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
        val tvStatus = view.findViewById<TextView>(R.id.tv_status)
        val btnDetail = view.findViewById<TextView>(R.id.btn_detail)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    lateinit var _context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        _context = parent.context
        val view: View = LayoutInflater.from(_context).inflate(R.layout.item_riwayat, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        val name = a.details[0].produk.name
        holder.tvNama.text = name
        holder.tvHarga.text = Helper().rupiah(a.total_transfer)
        holder.tvJumlah.text = a.total_item + " Items"
        holder.tvStatus.text = a.status

        // 2021-04-30 18:30:20 //24
        // jam 1   k || 01  kk
        // 09:20:20 am 12/pm/am
        holder.tvTangal.text = Helper().convertTanggal(a.created_at, "d MMM yyyy")

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            var color = _context.getColor(R.color.menungu)
            if (a.status == "SELESAI") color = _context.getColor(R.color.selesai)
            else if (a.status == "BATAL") color = _context.getColor(R.color.batal)
            holder.tvStatus.setTextColor(color)
        }else{
            var color = ContextCompat.getColor(_context, R.color.menungu);
            if (a.status == "SELESAI") color = ContextCompat.getColor(_context,R.color.selesai)
            else if (a.status == "BATAL") color = ContextCompat.getColor(_context,R.color.batal)
            holder.tvStatus.setTextColor(color)
        }

        holder.layout.setOnClickListener {
            listener.onClicked(a)
        }
    }

    interface Listeners {
        fun onClicked(data: Transaksi)
    }
}