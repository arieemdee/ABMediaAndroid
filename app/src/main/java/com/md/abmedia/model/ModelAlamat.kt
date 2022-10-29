package com.md.abmedia.model

/*
{
    "rajaongkir": {
        "query": [
        ],
        "status": {
            "code": 200,
            "description": "OK"
        },
        "results": [{
           "province_id": "12",
           "province": "Kalimantan Barat"
        },
        {
           "province_id": "13",
           "province": "Kalimantan Timur"
        }]
    }
}
*/

class ModelAlamat {
    val id = 0
    val nama = ""

    val status = Status()
    val results = ArrayList<Provinsi>()

    class Status {
        val code = 0
        val description = "OK"
    }
    class Provinsi {
        val province_id = "0"
        val province = ""
        val city_id = "0"
        val city_name = ""
        val postal_code = ""
        val type = ""
    }

}