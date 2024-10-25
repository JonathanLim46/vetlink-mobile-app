package com.example.vetlink.data.network

import com.example.vetlink.helper.Session
import com.example.vetlink.helper.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Create OkHttpClient with Interceptor for adding headers
    fun getOkHttpClient(session: SessionManager): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging) // Logging for debugging
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = session.getAuthToken()

                // Add Authorization header if token exists
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                // Add Accept header
                requestBuilder.addHeader("Accept", "application/json")

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    // Create Retrofit instance
    fun getRetrofit(session: SessionManager): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.100.22:8000/api/") // Replace with your base URL
            .client(getOkHttpClient(session))
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON conversion
            .build() // No need for CoroutineCallAdapterFactory
    }
}