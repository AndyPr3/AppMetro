package com.upc.appmetropolitano.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.TransactionModel
import com.upc.appmetropolitano.network.TransactionApiClient
import com.upc.appmetropolitano.repository.TransactionRepository

class TransactionViewModel (app: Application): AndroidViewModel(app) {
    private val repo = TransactionRepository(TransactionApiClient(app))

    val responseDetail = MutableLiveData<TransactionModel>()
    val errorMsg = MutableLiveData<String>()
    val saveStatus = MutableLiveData<Boolean>()

    fun register(cardNumber: String, paymentMethodId: Int, amount: Double, type: String, station: String,) {
        repo.register(cardNumber, paymentMethodId, amount, type, station) { res ->
            when (res) {
                is ApiResult.Success -> saveStatus.postValue(true)
                is ApiResult.Failure -> errorMsg.postValue(res.error.message)
            }
        }
    }

    fun detail(transactionId: Int) {
        repo.detail(transactionId) { res ->
            when (res) {
                is ApiResult.Success -> responseDetail.postValue(res.data)
                is ApiResult.Failure -> errorMsg.postValue(res.error.message)
            }
        }
    }

}