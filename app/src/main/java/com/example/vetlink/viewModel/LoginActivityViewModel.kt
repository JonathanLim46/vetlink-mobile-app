package com.example.vetlink.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetlink.data.model.auth.LoginResponse
import com.example.vetlink.repository.AuthRepository
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Identifier
import java.net.ConnectException
import java.net.SocketTimeoutException

class LoginActivityViewModel(private val repository: AuthRepository) : ViewModel() {

    val loginResponse = MutableLiveData<LoginResponse>()
    val errorMessage = MutableLiveData<String>()

    fun loginUser(identifier: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(identifier, password)

                // Check if the login was successful based on the response
                if (response.status == 200) {
                    // Successful login
                    loginResponse.postValue(response)
                    response.data.token?.let { token ->
                        repository.session.saveAuthToken(token)
                    }
                    Log.d("API_RESPONSE", "Login successful: $response")
                } else {
                    // Handle invalid credentials
                    errorMessage.postValue(response.message ?: "Invalid credentials")
                    Log.e("API_ERROR", "Invalid credentials: ${response.message}")
                }
            } catch (e: ConnectException) {
                errorMessage.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                errorMessage.postValue("Connection timed out. Please try again later.")
                Log.e("API_ERROR", "Timeout error: ${e.message}")
            } catch (e: Exception) {
                errorMessage.postValue("An error occurred. Please try again.") // Generic error message
                Log.e("API_ERROR", "Login error: ${e.message}")
            }
        }
    }
}