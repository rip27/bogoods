package com.example.bogoods.model

data class OrderModel(
    var key: String? = null,
    var idcart: String? = null,
    var idbarang: String? = null,
    var idorder: String? = null,
    var idpembeli: String? = null,
    var idpemilikstore: String? = null,
    var idstore: String? = null,
    var jumlah: String? = null,
    var tglpesan: String? = null,
    var totalbayar: String? = null,
    var statusbarang: String? = null,
    var pembayaran: String? = null,
    var statuspembayaran: String? = null,
    var alamatpengiriman: String? = null,
    var total: String? = null,
    var pengiriman: String? = null,
    var barangModel: ListBarangModel? = null,
    var pembeliModel: UserModel? = null,
    var storeModel: StoreModel? = null,
    var userModel: UserModel? = null
)