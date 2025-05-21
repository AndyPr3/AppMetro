package com.upc.appmetropolitano.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.upc.appmetropolitano.models.ApiError
import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.BusRouteModel

class BusRouteApiClient (private val ctx: Context){

    private val queue = VolleySingleton.getRequestQueue(ctx)

    fun routes(
        callback: (ApiResult<List<BusRouteModel>>) -> Unit
    ) {
        val url = ApiEndpoints.BUS_ROUTES
        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            { resp ->
                if (resp.optBoolean("success", false)) {
                    val arr = resp.getJSONArray("data")
                    val lista = List(arr.length()) { i ->
                        val o = arr.getJSONObject(i)
                        BusRouteModel(
                            code   = o.getString("code"),
                            type   = o.getString("type"),
                            schedule = o.optString("schedule", ""),
                            mapImagePath  = o.optString("mapImagePath", "")
                        )
                    }
                    callback(ApiResult.Success(lista))
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