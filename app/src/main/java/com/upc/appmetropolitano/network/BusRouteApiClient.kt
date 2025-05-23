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
                            mapImagePath  = o.optString("mapImagePath", ""),
                            isExpanded = o.optString("code","") == "A"
                        )
                    }
                    callback(ApiResult.Success(lista))
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