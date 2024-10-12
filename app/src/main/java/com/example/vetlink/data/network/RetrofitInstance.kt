package com.example.vetlink.data.network

import com.example.vetlink.helper.Session
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    fun getOkHttpClien(session: Session): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor{ chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = session.getToken()
                if (token != null){
                    //add header Authorization and bearer token
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                //header accept application/json
                requestBuilder.addHeader("Accept", "application/json")

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    fun getRetrofit(session: Session): Retrofit {
        val client = getOkHttpClien(session)

        return Retrofit.Builder()
            .baseUrl("http://192.168.100.69:8000/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}