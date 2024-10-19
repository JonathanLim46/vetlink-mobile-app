package com.example.vetlink.activity.auth

interface AuthListener {

    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)

}