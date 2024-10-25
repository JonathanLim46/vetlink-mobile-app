package com.example.vetlink.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetlink.data.model.auth.LogoutResponse
import com.example.vetlink.helper.SessionManager
import com.example.vetlink.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileFragmentViewModel(private val authRepository: AuthRepository): ViewModel() {

    val logoutResult = MutableLiveData<Response<LogoutResponse>>()
    val errorMessage = MutableLiveData<String>()

//    fun logoutUser(){
//        viewModelScope.launch {
//            try {
//                val response = authRepository.logout()
//                if (response.isSuccessful && response.body()?.status == 200){
//                    authRepository.session.clearAuthToken()
//                    logoutResult.postValue(response)
//                }else{
//                    errorMessage.postValue("Logout failed!")
//                }
//            } catch (e: Exception) {
//                errorMessage.postValue("An error occurred. Please try again.")
//            }
//        }
//    }
}