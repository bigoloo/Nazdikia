package com.bigoloo.interview.cafe.nazdikia.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter("client_id", "J1F4BZNJIT15MUG2G22GYM32GCLFIJIMW20JPZMGF23XLG11")
            .addQueryParameter("client_secret", "NKVHS123ETV5LS0YUC2BABOG3KQIMKL55N4JV2MRXV4MDZXF")
            .addQueryParameter("v", "20120609").build()

        val request = original.newBuilder().url(url).build()
        return chain.proceed(request)
    }

}