package com.upc.appmetropolitano.network
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

object VolleySingleton {
    private var instance: RequestQueue? = null

    fun getRequestQueue(ctx: Context): RequestQueue {
        return instance ?: synchronized(this) {
            val queue = Volley.newRequestQueue(ctx.applicationContext)
            instance = queue
            queue
        }
    }
}