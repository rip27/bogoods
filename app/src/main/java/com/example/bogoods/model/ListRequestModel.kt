package com.example.bogoods.model

data class ListRequestModel(
    var key: String? = null,
    var idreq: String? = null,
    var status: String? = null,
    var iduserrequest: String? = null,
    var idstore: String? = null,
    var userModel: UserModel? = null,
    var storeModel: StoreModel? = null
)