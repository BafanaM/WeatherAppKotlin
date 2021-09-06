package com.mankahla.weatherappkotlin.api

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(APP_ID, "ebb827e84b4656c6af3f8d359887dcf6")
            .build()

        val request = originalRequest.newBuilder().url(url).build()

        return chain.proceed(request)
    }

    companion object{
        const val APP_ID = "appid"
    }

}