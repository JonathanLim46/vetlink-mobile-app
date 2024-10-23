package com.example.vetlink.helper

import android.content.Context

class SessionManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)

    companion object{
        val TOKEN_KEY = "TOKEN"
    }

    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun getAuthToken(): String?{
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun clearAuthToken(){
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }

    fun isUserLoggedIn(): Boolean {
        return getAuthToken() != null
    }

}