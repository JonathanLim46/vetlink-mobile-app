package com.example.vetlink.activity.auth

import android.view.View
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    var email: String? = null
    var password: String? = null

    var authListener: AuthListener? = null

    fun onLoginButtonClick(view : View){
        authListener?.onStarted()
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            authListener?.onFailure("Invalid email or password")
            return
        }
        authListener?.onSuccess()
        //making login request
    }

}