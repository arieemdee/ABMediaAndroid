package com.md.abmedia.helper

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    fun rupiah(value:String):String{
        return NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(value))
    }
    fun rupiah(value:Int):String{
        return NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(value))
    }

    fun setToolbar(activity: Activity,toolbar: Toolbar,title:String){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun toastSort(context: Context,txt:String){
        Toast.makeText(context,txt,Toast.LENGTH_SHORT).show()
    }

    fun convertTanggal(tgl: String, formatBaru: String, fromatLama: String = "yyyy-MM-dd kk:mm:ss") :String{
        val dateFormat = SimpleDateFormat(fromatLama)
        val confert = dateFormat.parse(tgl)
        dateFormat.applyPattern(formatBaru)
        return dateFormat.format(confert)
    }

//    fun jts(sumber: Any, type: java.lang.reflect.Type): String? {
//        //val json = Gson().toJson(checkOut,CheckOut::class.java)
//        return Gson().toJson(sumber, type)
//    }
//    fun stj(sumber:String,obj:java.lang.Class<T>){
//        //val chekout = Gson().fromJson(json,CheckOut::class.java)
//    }

}