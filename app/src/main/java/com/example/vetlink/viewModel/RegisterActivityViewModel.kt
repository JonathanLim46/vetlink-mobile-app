package com.example.vetlink.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetlink.data.model.auth.LoginResponse
import com.example.vetlink.data.model.auth.RegisterResponse
import com.example.vetlink.repository.AuthRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.net.ConnectException

class RegisterActivityViewModel(private val repository: AuthRepository) : ViewModel() {

    val registerResponse = MutableLiveData<RegisterResponse?>()
    val errorMessage = MutableLiveData<String>()

    fun registerUser(
        name: String,
        username: String,
        email: String,
        password: String,
        phoneNumber: String,
        photo: MultipartBody.Part? = null
    ) {
        viewModelScope.launch {
            try {
                val response = repository.register(name, username, email, password, phoneNumber, photo)
                if (response.status == 201) { // Assuming response is a retrofit Response type
                    registerResponse.postValue(response)
                } else {
                    errorMessage.postValue("Registration failed: ${response.message}")
                }
            } catch (e: ConnectException) {
                errorMessage.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                errorMessage.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Registration error: ${e.message}")
            }
        }
    }

}