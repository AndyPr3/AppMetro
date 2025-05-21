package com.upc.appmetropolitano.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.upc.appmetropolitano.models.ApiResult
import com.upc.appmetropolitano.models.BusRouteModel
import com.upc.appmetropolitano.network.BusRouteApiClient
import com.upc.appmetropolitano.repository.BusRouteRepository

class BusRouteViewModel (app: Application): AndroidViewModel(app) {
    private val repo = BusRouteRepository(BusRouteApiClient(app))
    val errorMsg = MutableLiveData<String>()
    val responseBus = MutableLiveData<List<BusRouteModel>>()

    fun routes() {
        repo.routes() { res ->
            when (res) {
                is ApiResult.Success -> responseBus.postValue(res.data)
                is ApiResult.Failure -> errorMsg.postValue(res.error.message)
            }
        }
    }
}