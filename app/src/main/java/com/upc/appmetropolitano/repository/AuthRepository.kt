package com.upc.appmetropolitano.repository

import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.UserModel
import com.upc.appmetropolitano.network.AuthApiClient

class AuthRepository(private val api: AuthApiClient) {

    fun login(
        username: String,
        password: String,
        cb: (ApiResult<UserModel>) -> Unit
    ) {
        api.login(username, password, cb)
    }

    fun register(
        documentType: String,
        documentNumber: String,
        cardNumber: String,
        email: String,
        password: String,
        cb: (ApiResult<Unit>) -> Unit
    ) {
        api.register(
            documentType,
            documentNumber,
            cardNumber,
            email,
            password,
            cb
        )
    }
}