package com.example.bogoods.model

data class ListBarangModel (
    var key: String? = null,
    var namabarang: String? = null,
    var imagebarang: String? = null,
    var stok: String? = null,
    var desc: String? = null,
    var harga: String? = null,
    var idbarang: String? = null,
    var idstore: String? = null,
    var storemodel: StoreModel? = null
)