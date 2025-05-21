package com.upc.appmetropolitano.network

object ApiEndpoints {

    private const val BASE = "http://localhost:3000"
    const val AUTH_LOGIN = "$BASE/auth/login"
    const val AUTH_REGISTER = "$BASE/auth/register"
    const val CARD_INFO = "$BASE/card/info"
    const val CARD_HISTORY = "$BASE/card/history"
    const val TRANSACTION_REGISTER = "$BASE/transaction/register"
    const val TRANSACTION_DETAIL = "$BASE/transaction/detail"
    const val BUS_ROUTES = "$BASE/bus/routes"
}