package com.upc.appmetropolitano.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.CardModel
import com.upc.appmetropolitano.models.TransactionModel
import com.upc.appmetropolitano.network.CardApiClient
import com.upc.appmetropolitano.repository.CardRepository

class CardViewModel (app: Application): AndroidViewModel(app) {
    private val repo = CardRepository(CardApiClient(app))
    val cardInfo = MutableLiveData<CardModel>()
    val cardHistory = MutableLiveData<List<TransactionModel>>()
    val errorMsg = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun info(userId: Int, cardId: Int) {
        loading.postValue(true)
        repo.info(userId, cardId) { res ->
            loading.postValue(false)
            when (res) {
                is ApiResult.Success -> cardInfo.postValue(res.data)
                is ApiResult.Failure -> errorMsg.postValue(res.error.message)
            }
        }
    }

    fun history(cardId: Int, year: Int, month: Int) {
        loading.postValue(true)
        repo.history(cardId, year, month) { res ->
            loading.postValue(false)
            when (res) {
                is ApiResult.Success -> cardHistory.postValue(res.data)
                is ApiResult.Failure -> errorMsg.postValue(res.error.message)
            }
        }
    }
}