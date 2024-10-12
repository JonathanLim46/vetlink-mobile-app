package com.example.vetlink.helper

import android.content.Context

class Session(context: Context) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object{
        const val TOKEN_KEY = "token"
    }

    fun setToken(token: String){
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clearToken(){
        prefs.edit().remove(TOKEN_KEY).apply()
    }

}