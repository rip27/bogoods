package com.example.bogoods.model

data class RatingModel(
    var key: String? = null,
    var idrating: String? = null,
    var rating: Float? = null,
    var comment: String? = null,
    var userModel: UserModel? = null
)