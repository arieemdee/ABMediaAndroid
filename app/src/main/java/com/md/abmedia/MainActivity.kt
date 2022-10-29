package com.md.abmedia

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.md.abmedia.activity.LoginActivity
import com.md.abmedia.activity.MasukActivity
import com.md.abmedia.fragment.AkunFragment
import com.md.abmedia.fragment.HomeFragment
import com.md.abmedia.fragment.KeranjangFragment
import com.md.abmedia.helper.SharedPref

class MainActivity : AppCompatActivity() {

    val fragmentHome: Fragment = HomeFragment()
    val fragmentAkun: Fragment = AkunFragment()
    val fragmenKeranjang: Fragment = KeranjangFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private  lateinit var bottomNavigationView: BottomNavigationView

    private var statusLogin = false;
    private lateinit var s:SharedPref
    private var dariDetail:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        s = SharedPref(this)
        setUpBottomNav()
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessage, IntentFilter("event:keranjang"))
    }
    val mMessage:BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            dariDetail= true
        }
    }
    private fun setUpBottomNav(){
        fm.beginTransaction().add(R.id.container,fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container,fragmentAkun).hide(fragmentAkun).commit()
        fm.beginTransaction().add(R.id.container,fragmenKeranjang).hide(fragmenKeranjang).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnItemSelectedListener  { item -> false

            when(item.itemId) {
                R.id.navigation_home -> {
                    callFragment(0,fragmentHome)
                }
                R.id.navigation_keranjang -> {
                    callFragment(1,fragmenKeranjang)
                }
                R.id.navigation_akun -> {
                    if(s.getStatusLogin()){
                        callFragment(2,fragmentAkun)
                    }else{
                        startActivity(Intent(this,MasukActivity::class.java))
                    }

                }
            }
            false
        }
    }
    private fun callFragment(int: Int, fragment: Fragment){
        Log.d("TAG", "onCreate: $fragment")
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    override fun onResume() {
        if(dariDetail) {
            dariDetail = false
            callFragment(1,fragmenKeranjang)
        }
        super.onResume()
    }
}