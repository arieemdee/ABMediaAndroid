package com.md.abmedia.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.md.abmedia.MainActivity
import com.md.abmedia.R
import com.md.abmedia.activity.LoginActivity
import com.md.abmedia.activity.RiwayatActivity
import com.md.abmedia.databinding.FragmentAkunBinding
import com.md.abmedia.helper.SharedPref

class AkunFragment : Fragment() {
    lateinit var s:SharedPref
    /*
        lateinit var btnLogout: TextView
        lateinit var tvNama: TextView
        lateinit var tvEmail: TextView
        lateinit var tvPhone: TextView
    */
    private var _binding:FragmentAkunBinding?=null
    private val _bdg get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAkunBinding.inflate(inflater,container,false)
        //val view: View = inflater.inflate(R.layout.fragment_akun, container, false)
        s= SharedPref(requireActivity())
        mainButton()
        setData()
        return _bdg.root // view
        // init(view)
        //return view
    }

    private fun mainButton() {
        _bdg.btnLogout.setOnClickListener {
            s.setStatusLogin(false)
        }
        _bdg.btnRiwayat.setOnClickListener {
            startActivity(Intent(requireActivity(),RiwayatActivity::class.java))
        }
    }


    fun setData(){
        if(s.getUser()==null){ // menjebak krn saat awal pake get userny satu2
            val inten = Intent(activity,LoginActivity::class.java)
            inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(inten)
            return
        }
        val user = s.getUser()!!
        _bdg.tvNama.text = user.name
        _bdg.tvEmail.text = user.email
        _bdg.tvPhone.text = user.phone
    }
    /*
        private fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
    }
    */



}