package com.upc.appmetropolitano.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.upc.appmetropolitano.models.ApiError
import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.CardModel
import com.upc.appmetropolitano.models.TransactionModel

class CardApiClient (private val ctx: Context){

    private val queue = VolleySingleton.getRequestQueue(ctx)

    fun info(
        userId: Int,
        cardId: Int,
        callback: (ApiResult<CardModel>) -> Unit
    ) {
        val url = "${ApiEndpoints.CARD_INFO}/?userId=$userId&cardId=$cardId"
        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            { resp ->
                if (resp.optBoolean("success", false)) {
                    val o = resp.getJSONObject("data")
                    val arr = o.getJSONArray("history")
                    val history = List(arr.length()) { i ->
                        val obj = arr.getJSONObject(i)
                        TransactionModel(
                            transactionId = obj.getInt("transactionId"),
                            cardNumber = o.getString("cardNumber"),
                            amount = obj.getDouble("amount"),
                            type = obj.optString("type", ""),
                            station = obj.optString("station", ""),
                            paymentMethodId = obj.getInt("paymentMethodId"),
                            transactionDate = obj.getString("transactionDate"),
                            notes = obj.getString("notes")
                        )
                    }
                    val model = CardModel(
                        cardId = o.getInt("cardId"),
                        cardNumber = o.getString("cardNumber"),
                        balance = o.getDouble("balance"),
                        isDefault = o.optBoolean("isDefault", false),
                        history = history
                    )
                    callback(ApiResult.Success(model))
                } else {
                    val errObj = resp.optJSONObject("error")
                    if(errObj!=null){
                        val apiErr = ApiError(
                            code    = errObj.getString("code"),
                            message = errObj.getString("message")
                        )
                        callback(ApiResult.Failure(apiErr))
                    } else {
                        callback(ApiResult.Failure(
                            ApiError("UNKNOWN_ERROR", "Error al consultar el servicio.")
                        ))
                    }
                }
            },
            { volleyErr ->
                callback(
                    ApiResult.Failure(
                        ApiError("NETWORK_ERROR", volleyErr.message ?: "Error de red")
                    ))
            }
        )
        queue.add(req)
    }

    fun history(
        cardId: Int,
        year: Int,
        month: Int,
        callback: (ApiResult<List<TransactionModel>>) -> Unit
    ) {
        val url = "${ApiEndpoints.CARD_HISTORY}/?cardId=$cardId&year=$year&month=$month"
        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            { resp ->
                if (resp.optBoolean("success", false)) {
                    val arr = resp.getJSONArray("data")
                    val list = List(arr.length()) { i ->
                        val o = arr.getJSONObject(i)
                        TransactionModel(
                            transactionId = o.getInt("transactionId"),
                            cardNumber = o.optString("cardNumber",""),
                            amount = o.getDouble("amount"),
                            type = o.optString("type", ""),
                            station = o.optString("station", ""),
                            paymentMethodId = o.getInt("paymentMethodId"),
                            transactionDate = o.getString("transactionDate"),
                            notes = o.optString("notes","")
                        )
                    }
                    callback(ApiResult.Success(list))
                } else {
                    val errObj = resp.optJSONObject("error")
                    if(errObj!=null){
                        val apiErr = ApiError(
                            code    = errObj.getString("code"),
                            message = errObj.getString("message")
                        )
                        callback(ApiResult.Failure(apiErr))
                    } else {
                        callback(ApiResult.Failure(
                            ApiError("UNKNOWN_ERROR", "Error al consultar el servicio.")
                        ))
                    }
                }
            },
            { volleyErr ->
                callback(
                    ApiResult.Failure(
                        ApiError("NETWORK_ERROR", volleyErr.message ?: "Error de red")
                    ))
            }
        )
        queue.add(req)
    }
}