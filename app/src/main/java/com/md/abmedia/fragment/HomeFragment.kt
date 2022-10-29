package com.md.abmedia.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.md.abmedia.R
import com.md.abmedia.adapter.AdapterProduk
import com.md.abmedia.app.ApiConfig
import com.md.abmedia.databinding.FragmentHomeBinding
import com.md.abmedia.model.Produk
import com.md.abmedia.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
/*
    lateinit var _vpSlider: ViewPager
    lateinit var _rvProduk: RecyclerView
    lateinit var _rvProdukTerlaris: RecyclerView
    lateinit var _rvElektronik: RecyclerView
*/
    private var _binding: FragmentHomeBinding? = null
    private val _bdg get() = _binding!!

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        //val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        //init(view)
        getProduk()
        return _bdg.root // view
    }
    fun displayProduk(){
        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slider1)
        arrSlider.add(R.drawable.slider2)
        arrSlider.add(R.drawable.slider3)
        val adapterSlider = AdapterSlider(arrSlider,activity)
        _bdg.vpSlider.adapter = adapterSlider

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        _bdg.rvProduk.adapter = AdapterProduk(requireActivity(),listProduk)
        _bdg.rvProduk.layoutManager = layoutManager

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        _bdg.rvProdukTerlaris.adapter =AdapterProduk(requireActivity(),listProduk)
        _bdg.rvProdukTerlaris.layoutManager = layoutManager2

        val layoutManager3 = LinearLayoutManager(activity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL
        _bdg.rvElektronik.adapter =AdapterProduk(requireActivity(),listProduk)
        _bdg.rvElektronik.layoutManager = layoutManager3
    }
    private var listProduk:ArrayList<Produk> = ArrayList()
    private fun getProduk(){
        ApiConfig.instanceRetrofit.getProduk().enqueue(object :
            Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if(res.success == 1){
                    val arrPrd = ArrayList<Produk>()
                    for(p in res.produks){
                        p.discount = 10000
                        arrPrd.add(p)
                    }
                    listProduk = arrPrd
                    displayProduk()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                //pb.visibility = View.GONE
                //Toast.makeText(this@LoginActivity,"Error"+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
/*
fun init(view: View){
    _vpSlider = view.findViewById(R.id.vp_slider)
    _rvProduk = view.findViewById(R.id.rv_produk)
    _rvProdukTerlaris = view.findViewById(R.id.rv_produkTerlaris)
    _rvElektronik = view.findViewById(R.id.rv_elektronik)
}
*/

/*

    val arrProduk: ArrayList<Produk>get(){
        val arr = ArrayList<Produk>()
        val p1 = Produk()
        p1.nama = "HP Core I3"
        p1.harga = "Rp.5.900.000"
        p1.gambar = R.drawable.asset_product_laptop

        val p2 = Produk()
        p2.nama = "Baju Wanita"
        p2.harga = "Rp.5.100.000"
        p2.gambar = R.drawable.asset_cat_jumpsuti_wanita

        val p3 = Produk()
        p3.nama = "Kerdus"
        p3.harga = "Rp.5.000.000"
        p3.gambar = R.drawable.product

        arr.add(p1)
        arr.add(p2)
        arr.add(p3)
        return arr
    }
    val arrElektronik: ArrayList<Produk>get(){
        val arr = ArrayList<Produk>()
        val p1 = Produk()
        p1.nama = "HP Core I3"
        p1.harga = "Rp.5.900.000"
        p1.gambar = R.drawable.asset_product_laptop

        val p2 = Produk()
        p2.nama = "Baju Wanita"
        p2.harga = "Rp.5.100.000"
        p2.gambar = R.drawable.asset_cat_jumpsuti_wanita

        val p3 = Produk()
        p3.nama = "Kerdus"
        p3.harga = "Rp.5.000.000"
        p3.gambar = R.drawable.product

        arr.add(p1)
        arr.add(p2)
        arr.add(p3)
        return arr
    }
    val arrProdukTerlaris: ArrayList<Produk>get(){
        val arr = ArrayList<Produk>()
        val p1 = Produk()
        p1.nama = "HP Core I3"
        p1.harga = "Rp.5.900.000"
        p1.gambar = R.drawable.asset_product_laptop

        val p2 = Produk()
        p2.nama = "Baju Wanita"
        p2.harga = "Rp.5.100.000"
        p2.gambar = R.drawable.asset_cat_jumpsuti_wanita

        val p3 = Produk()
        p3.nama = "Kerdus"
        p3.harga = "Rp.5.000.000"
        p3.gambar = R.drawable.product

        arr.add(p1)
        arr.add(p2)
        arr.add(p3)
        return arr
    }

*/

}