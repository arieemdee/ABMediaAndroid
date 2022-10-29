package com.md.abmedia.model

class Transaksi {
/*
{
  "success": 1,
  "message": "Transaksi Berhasil",
  "transaksi": {
    "bank": "BRI",
    "jasa_pengiriman": "OKE",
    "kurir": "JNE",
    "name": "ariiyanto",
    "ongkir": "12000",
    "phone": "08562501188",
    "total_harga": "52000",
    "total_item": "1",
    "total_transfer": "64000",
    "user_id": "0",
    "kode_payment": "INV\/PYM\/2022-10-23\/740",
    "kode_trx": "INV\/PYM\/2022-10-23\/813",
    "kode_unik": 105,
    "status": "MENUNGGU",
    "expired_at": "2022-10-24T01:17:02.821134Z",
    "updated_at": "2022-10-23 01:17:02",
    "created_at": "2022-10-23 01:17:02",
    "id": 27
  }
}
*/
    var id= 0
    var bank= ""
    var jasa_pengiriman=""
    var kurir= ""
    var name= ""
    var ongkir=  ""
    var phone= ""
    var total_harga= ""
    var total_item= ""
    var total_transfer= ""
    var detail_lokasi = ""
    var user_id= ""
    var kode_payment= ""
    var kode_trx= ""
    var kode_unik= 0
    var status= ""
    var expired_at= ""
    var updated_at= ""
    var created_at= ""
    val details = ArrayList<DetailTransaksi>()
}