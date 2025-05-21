package com.upc.appmetropolitano.models

data class BusRouteModel(
    val code: String,
    val type: String,
    val schedule: String,
    val mapImagePath: String,
    var isExpanded: Boolean = false
)
