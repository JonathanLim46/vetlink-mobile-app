package com.example.vetlink.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetlink.data.model.user.User
import com.example.vetlink.repository.AuthRepository
import kotlinx.coroutines.launch
import java.net.ConnectException

class MainActivityViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _errorMessageUser = MutableLiveData<String>()
    val errorMessageUser: LiveData<String> get() = _errorMessageUser

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess

    fun fetchUser() {
        viewModelScope.launch {
            try {
                val responseUser = authRepository.getProfile()
                _user.postValue(responseUser.data)
            } catch (e: ConnectException) {
                _errorMessageUser.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageUser.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Login error: ${e.message}")
            }
        }
    }

    fun performLogout(){
        viewModelScope.launch {
            val responseLogout = authRepository.logout()
            _logoutSuccess.postValue(responseLogout)
        }
    }

}
