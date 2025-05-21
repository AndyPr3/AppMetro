package com.upc.appmetropolitano.models

data class TransactionModel (
    val transactionId: Int,
    val cardNumber: String,
    val amount: Double,
    val type: String,
    val station: String,
    val paymentMethodId: Int,
    val transactionDate: String,
    val notes: String = ""
)