package com.example.bogoods.model

data class CartModel(
    var key: String? = null,
    var idcart: String? = null,
    var idbarang: String? = null,
    var idpembeli: String? = null,
    var idstore: String? = null,
    var jumlah: String? = null,
    var barangModel: ListBarangModel? = null,
    var pembeliModel: UserModel? = null,
    var storeModel: StoreModel? = null
)