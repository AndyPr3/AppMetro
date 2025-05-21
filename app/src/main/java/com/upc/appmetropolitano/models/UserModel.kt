package com.upc.appmetropolitano.models

data class UserModel(
    val userId: Int,
    val email: String,
    val firtsName : String,
    val fullName: String,
    val documentNumber: String,
    val phone: String,
    val cardNumber: String,
    val defaultCardId : Int
)