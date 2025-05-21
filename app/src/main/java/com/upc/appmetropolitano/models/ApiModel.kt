package com.upc.appmetropolitano.models

data class ApiError(
    val code: String,
    val message: String
)

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T): ApiResult<T>()
    data class Failure(val error: ApiError): ApiResult<Nothing>()
}