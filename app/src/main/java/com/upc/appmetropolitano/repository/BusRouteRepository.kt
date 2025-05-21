package com.upc.appmetropolitano.repository

import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.BusRouteModel
import com.upc.appmetropolitano.network.BusRouteApiClient

class BusRouteRepository(private val api: BusRouteApiClient ) {

    fun routes(
        cb: (ApiResult<List<BusRouteModel>>) -> Unit
    ) {
        api.routes(cb)
    }
}