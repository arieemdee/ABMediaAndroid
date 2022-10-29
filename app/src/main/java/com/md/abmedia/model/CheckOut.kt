package com.md.abmedia.model

class CheckOut {
    /*
{
	"user_id":"3",
	"total_item":"6",
	"total_harga":"285000",
	"name":"syafiq",
	"phone":"08562501188",
	"produk":[
		{
			"id":"4",
			"total_item":"4",
			"total_harga":"57000000",
			"catatan":"catatan ku"
		},
		{
			"id":"5",
			"total_item":"2",
			"total_harga":"52000",
			"catatan":"catatan ku"
		}

	]
}
*/
    lateinit var user_id:String
    lateinit var total_item:String
    lateinit var total_harga:String
    lateinit var name:String
    lateinit var phone:String
    lateinit var kurir:String
    lateinit var detail_lokasi:String
    lateinit var jasa_pengiriman:String
    lateinit var ongkir:String
    lateinit var total_transfer:String
    lateinit var bank:String
    var produks = ArrayList<Item>()
    class Item {
        lateinit var id:String
        lateinit var total_item:String
        lateinit var total_harga:String
        lateinit var catatan:String
    }
}