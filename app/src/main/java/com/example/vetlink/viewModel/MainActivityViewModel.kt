package com.example.vetlink.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetlink.data.model.pets.Pet
import com.example.vetlink.data.model.user.User
import com.example.vetlink.data.model.veteriner.Veteriner
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.repository.PetRepository
import com.example.vetlink.repository.VeterinerRepository
import kotlinx.coroutines.launch
import okhttp3.Response
import java.net.ConnectException
import kotlin.math.log

class MainActivityViewModel(
    private val authRepository: AuthRepository,
    private val veterinerRepository: VeterinerRepository?
): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _errorMessageUser = MutableLiveData<String>()
    val errorMessageUser: LiveData<String> get() = _errorMessageUser

//    private val _pets = MutableLiveData<List<Pet>>()
//    val pets: LiveData<List<Pet>> get() = _pets

//    private val _errorMessagePet = MutableLiveData<String>()
//    val errorMessagePet: LiveData<String> get() = _errorMessagePet

    private val _veteriners = MutableLiveData<List<Veteriner>>()
    val veteriners: LiveData<List<Veteriner>> get() = _veteriners

    private val _errorMessageVeteriner = MutableLiveData<String>()
    val errorMessageVeteriner: LiveData<String> get() = _errorMessageVeteriner

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
                 Log.e("API_ERROR", "Fetch user error: ${e.message}")
            }
        }
    }

    fun getVeteriners() {
        viewModelScope.launch {
            try {
                val responseVeteriners = veterinerRepository?.getVeteriners()
                if (responseVeteriners?.isSuccess == true) {
                    val veterinerList = responseVeteriners.getOrNull()?.data ?: emptyList()
                    _veteriners.postValue(veterinerList)
                    Log.d("API_RESPONSE", "Veteriners fetched successfully: ${veterinerList.size}")
                } else {
                    _errorMessageVeteriner.postValue("Failed to fetch veteriners.")
                    Log.e("API_ERROR", "Veteriners fetch error: ${responseVeteriners?.exceptionOrNull()?.message}")
                }
            }catch (e: ConnectException) {
                _errorMessageUser.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageUser.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Fetch user error: ${e.message}")
            }
        }
    }



//    fun getPets(){
//        viewModelScope.launch {
//            try {
//                val responsePets = petRepository?.getPets()
//                if (responsePets!!.isSuccess){
//                    _pets.postValue(responsePets.getOrNull()?.data)
//                    Log.d("API_RESPONSE", "Pets fetched successfully: ${responsePets.getOrNull()?.data}")
//                }else{
//                    _errorMessagePet.postValue("An error occurred. Please try again.")
//                }
//            }catch (e: ConnectException) {
//                _errorMessageUser.postValue("Unable to connect to the server. Please check your internet connection.")
//                Log.e("API_ERROR", "Network error: ${e.message}")
//            } catch (e: Exception) {
//                _errorMessageUser.postValue("An error occurred. Please try again.")
//                Log.e("API_ERROR", "Get Pets error: ${e.message}")
//            }
//        }
//    }

    fun performLogout(){
        viewModelScope.launch {
            val responseLogout = authRepository.logout()
            _logoutSuccess.postValue(responseLogout)
        }
    }

}
