package com.example.vetlink.data.network

import com.example.vetlink.helper.Session
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    fun getOkHttpClient(session: Session): OkHttpClient{
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
            .addInterceptor( HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    fun getRetrofit(session: Session): Retrofit {
        val client = getOkHttpClient(session)

        return Retrofit.Builder()
            .baseUrl("http://192.168.100.69:8000/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}