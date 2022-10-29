package com.md.abmedia.app

import com.md.abmedia.model.CheckOut
import com.md.abmedia.model.ResponModel
import com.md.abmedia.model.rajaongkir.ResponOngkir
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("fcm") fcm: String
    ):Call<ResponModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("fcm") fcm: String
    ):Call<ResponModel>

    @POST("chekout")
    fun checkout(
        @Body data:CheckOut
    ):Call<ResponModel>

    @GET("produk")
    fun getProduk():Call<ResponModel>

    //https://api.rajaongkir.com/starter/province
    @GET("province")
    fun getPropinsi(
        @Header("key") key:String
    ):Call<ResponModel>

    //https://api.rajaongkir.com/starter/city
    @GET("city")
    fun getKota(
        @Header("key") key:String,
        @Query("province") id:String
    ):Call<ResponModel>

    //https://dev.farizdotid.com/api/daerahindonesia/kecamatan?id_kota=3214
    @GET("kecamatan")
    fun getKecamatan(
        @Query("id_kota") id:Int
    ):Call<ResponModel>

    //https://api.rajaongkir.com/starter/cost
    @FormUrlEncoded
    @POST("cost")
    fun ongkir(
        @Header("key") key:String,
        @Field("origin") origin:String,
        @Field("destination") destination:String,
        @Field("weight") weight:Int,
        @Field("courier") courier:String
    ):Call<ResponOngkir>

    @GET("chekout/user/{id}")
    fun getRiwayat(
        @Path("id") id:Int
    ):Call<ResponModel>

    @POST("chekout/batal/{id}")
    fun batalCheckout(
        @Path("id") id:Int
    ):Call<ResponModel>
}