package com.upc.appmetropolitano.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.upc.appmetropolitano.models.ApiError
import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.UserModel
import org.json.JSONObject

class AuthApiClient (private val ctx: Context){
    private val queue = VolleySingleton.getRequestQueue(ctx)

    fun login(
        username: String,
        password: String,
        callback: (ApiResult<UserModel>) -> Unit
    ) {
        val url = ApiEndpoints.AUTH_LOGIN
        val json = JSONObject().apply {
            put("username",   username)
            put("password",   password)
        }
        val req = JsonObjectRequest(
            Request.Method.POST, url, json,
            { resp ->
                if (resp.optBoolean("success", false)) {
                    val obj = resp.getJSONObject("data")
                    val model = UserModel(
                        userId   = obj.getInt("userId"),
                        email   = obj.getString("email"),
                        firtsName = obj.getString("firtsName"),
                        fullName = obj.getString("fullName"),
                        documentNumber = obj.getString("documentNumber"),
                        phone = obj.getString("phone"),
                        cardNumber = obj.getString("cardNumber"),
                        defaultCardId = obj.getInt("defaultCardId")
                    )
                    callback(ApiResult.Success(model))
                } else {
                    val errObj = resp.getJSONObject("error")
                    val apiErr = ApiError(
                        code    = errObj.getString("code"),
                        message = errObj.getString("message")
                    )
                    callback(ApiResult.Failure(apiErr))
                }
            },
            { volleyErr ->
                callback(ApiResult.Failure(
                    ApiError("NETWORK_ERROR", volleyErr.message ?: "Error de red")
                ))
            }
        )
        queue.add(req)
    }

    fun register(
        documentType: String,
        documentNumber: String,
        cardNumber: String,
        email: String,
        password: String,
        callback: (ApiResult<Unit>) -> Unit
    ) {
        val url = ApiEndpoints.AUTH_REGISTER
        val json = JSONObject().apply {
            put("documentType",   documentType)
            put("documentNumber",   documentNumber)
            put("cardNumber", cardNumber)
            put("email",  email)
            put("password",  password)
        }

        val req = JsonObjectRequest(
            Request.Method.POST, url, json,
            { resp ->
                if (resp.optBoolean("success", false)) {
                    callback(ApiResult.Success(Unit))
                } else {
                    val err = resp.getJSONObject("error")
                    callback(ApiResult.Failure(
                        ApiError(err.getString("code"), err.getString("message"))
                    ))
                }
            },
            { volleyErr ->
                callback(ApiResult.Failure(
                    ApiError("NETWORK_ERROR", volleyErr.message ?: "Error de red")
                ))
            }
        )
        queue.add(req)
    }
}