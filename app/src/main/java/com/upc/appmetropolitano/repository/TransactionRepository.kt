package com.upc.appmetropolitano.repository

import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.TransactionModel
import com.upc.appmetropolitano.network.TransactionApiClient

class TransactionRepository(private val api: TransactionApiClient) {

    fun register(
        cardNumber: String,
        paymentMethodId: Int,
        amount: Double,
        type: String,
        station: String,
        cb: (ApiResult<Unit>) -> Unit
    ) {
        api.register(
            cardNumber,
            paymentMethodId,
            amount,
            type,
            station,
            cb
        )
    }

    fun detail(
        transaction: Int,
        cb: (ApiResult<TransactionModel>) -> Unit
    ) {
        api.detail(transaction, cb)
    }



}