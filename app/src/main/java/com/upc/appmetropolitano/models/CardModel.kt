package com.upc.appmetropolitano.models

data class CardModel (
    val cardId: Int,
    val cardNumber: String,
    val balance: Double,
    val isDefault: Boolean,
    val history: List<TransactionModel> = emptyList()
)