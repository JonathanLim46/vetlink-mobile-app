package com.example.vetlink.data.network

import com.example.vetlink.helper.DateDeserializer
import com.example.vetlink.helper.SessionManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

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

    // Create custom Gson instance with Date deserializer
    private fun getGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer()) // Using the custom Date deserializer
            .create()
    }

    // Create Retrofit instance
    fun getRetrofit(session: SessionManager): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://vetlink-edfsdff6e3e8bhcy.westindia-01.azurewebsites.net/api/") // Replace with your base URL
            .client(getOkHttpClient(session))
            .addConverterFactory(GsonConverterFactory.create(getGson())) // Use Gson for JSON conversion
            .build()
    }
}