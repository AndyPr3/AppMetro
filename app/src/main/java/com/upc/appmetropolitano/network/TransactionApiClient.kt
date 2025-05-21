package com.upc.appmetropolitano.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.upc.appmetropolitano.models.ApiError
import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.TransactionModel
import org.json.JSONObject

class TransactionApiClient (private val ctx: Context){

    private val queue = VolleySingleton.getRequestQueue(ctx)

    fun register(
        cardNumber: String,
        paymentMethodId: Int,
        amount: Double,
        type: String,
        station: String,
        callback: (ApiResult<Unit>) -> Unit
    ) {
        val url = ApiEndpoints.TRANSACTION_REGISTER
        val json = JSONObject().apply {
            put("cardNumber",   cardNumber)
            put("paymentMethodId",   paymentMethodId)
            put("amount", amount)
            put("type",  type)
            put("station",  station)
        }

        val req = JsonObjectRequest(
            Request.Method.POST, url, json,
            { resp ->
                if (resp.optBoolean("success", false)) {
                    callback(ApiResult.Success(Unit))
                } else {
                    val err = resp.getJSONObject("error")
                    callback(
                        ApiResult.Failure(
                        ApiError(err.getString("code"), err.getString("message"))
                    ))
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

    fun detail(
        transactionId: Int,
        callback: (ApiResult<TransactionModel>) -> Unit
    ) {
        val url = "${ApiEndpoints.TRANSACTION_DETAIL}/?transactionId=$transactionId"
        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            { resp ->
                if (resp.optBoolean("success", false)) {
                    val o = resp.getJSONObject("data")
                    val model = TransactionModel(
                        transactionId = o.optInt("transactionId", 0),
                        cardNumber = o.optString("cardNumber",""),
                        amount = o.getDouble("amount"),
                        type = o.optString("type", ""),
                        station = o.optString("station", ""),
                        paymentMethodId = o.getInt("paymentMethodId"),
                        transactionDate = o.getString("transactionDate")
                    )
                    callback(ApiResult.Success(model))
                } else {
                    val err = resp.getJSONObject("error")
                    callback(
                        ApiResult.Failure(
                            ApiError(err.getString("code"), err.getString("message"))
                        ))
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