package com.upc.appmetropolitano.repository

import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.CardModel
import com.upc.appmetropolitano.models.TransactionModel
import com.upc.appmetropolitano.network.CardApiClient

class CardRepository(private val api: CardApiClient) {

    fun info(
        userId: Int,
        cardId: Int,
        cb: (ApiResult<CardModel>) -> Unit
    ) {
        api.info(userId, cardId, cb)
    }

    fun history(
        cardId: Int,
        year: Int,
        month: Int,
        cb: (ApiResult<List<TransactionModel>>) -> Unit
    ) {
        api.history(cardId, year, month, cb)
    }
}