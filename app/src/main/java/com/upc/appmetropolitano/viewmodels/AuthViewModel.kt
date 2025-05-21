package com.upc.appmetropolitano.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.UserModel
import com.upc.appmetropolitano.network.AuthApiClient
import com.upc.appmetropolitano.repository.AuthRepository

class AuthViewModel (app: Application): AndroidViewModel(app) {
    private val repo = AuthRepository(AuthApiClient(app))
    val user = MutableLiveData<UserModel>()
    val errorMsg = MutableLiveData<String>()
    val saveStatus = MutableLiveData<Boolean>()

    fun login(email: String, password: String) {
        repo.login(email, password) { res ->
            when (res) {
                is ApiResult.Success -> user.postValue(res.data)
                is ApiResult.Failure -> errorMsg.postValue(res.error.message)
            }
        }
    }

    fun register(documentType: String, documentNumber: String, cardNumber: String, email: String, password: String) {
        repo.register(documentType, documentNumber, cardNumber, email, password) { res ->
            when (res) {
                is ApiResult.Success -> saveStatus.postValue(true)
                is ApiResult.Failure -> errorMsg.postValue(res.error.message)
            }
        }
    }
}