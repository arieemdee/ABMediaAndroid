package com.md.abmedia.model

class ResponModel {
    var success = 0
    lateinit var message:String
    var user = User()

    //{"success":1,"message":"Get Produk Berhasil","produks":[{"id":4,"name":"Leptop leknopo","harga":"57000000","deskripsi":"Lagi ada promo","category_id":1,"image":"102022140922358_asset_product_laptop.png","created_at":"2022-10-14 09:46:22","updated_at":"2022-10-14 09:46:22"},{"id":5,"name":"Baju istri","harga":"52000","deskripsi":"Baju bekas istriku","category_id":1,"image":"102022140954170_asset_cat_jumpsuti_wanita.png","created_at":"2022-10-14 09:46:54","updated_at":"2022-10-14 09:46:54"},{"id":6,"name":"HP OPO","harga":"45000","deskripsi":"HP OPO mas","category_id":1,"image":"102022140929676_slider2.png","created_at":"2022-10-14 09:47:29","updated_at":"2022-10-14 09:47:29"}]}
    //var produks = Produk()

    //{"success":1,"message":"Get Produk Berhasil","produks":[{"id":4,"name":"Leptop leknopo","harga":"57000000","deskripsi":"Lagi ada promo","category_id":1,"image":"102022140922358_asset_product_laptop.png","created_at":"2022-10-14 09:46:22","updated_at":"2022-10-14 09:46:22"},{"id":5,"name":"Baju istri","harga":"52000","deskripsi":"Baju bekas istriku","category_id":1,"image":"102022140954170_asset_cat_jumpsuti_wanita.png","created_at":"2022-10-14 09:46:54","updated_at":"2022-10-14 09:46:54"},{"id":6,"name":"HP OPO","harga":"45000","deskripsi":"HP OPO mas","category_id":1,"image":"102022140929676_slider2.png","created_at":"2022-10-14 09:47:29","updated_at":"2022-10-14 09:47:29"}]}
    var produks:ArrayList<Produk> = ArrayList()
    var transaksis:ArrayList<Transaksi> = ArrayList()

    var rajaongkir = ModelAlamat()
    var transaksi = Transaksi()

    var provinsi:ArrayList<ModelAlamat> = ArrayList()
    var kota_kabupaten:ArrayList<ModelAlamat> = ArrayList()
    var kecamatan:ArrayList<ModelAlamat> = ArrayList()
}