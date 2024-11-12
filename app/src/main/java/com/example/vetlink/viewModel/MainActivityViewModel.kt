package com.example.vetlink.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetlink.data.model.forums.Forum
import com.example.vetlink.data.model.pets.Pet
import com.example.vetlink.data.model.queue.LatestQueue
import com.example.vetlink.data.model.queue.Queue
import com.example.vetlink.data.model.user.User
import com.example.vetlink.data.model.veteriner.Veteriner
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.repository.ForumRepository
import com.example.vetlink.repository.PetRepository
import com.example.vetlink.repository.QueueRepository
import com.example.vetlink.repository.VeterinerRepository
import kotlinx.coroutines.launch
import okhttp3.Response
import java.net.ConnectException
import kotlin.math.log

class MainActivityViewModel(
    private val authRepository: AuthRepository,
    private val veterinerRepository: VeterinerRepository?,
    private val queueRepository: QueueRepository?,
    private val forumRepository: ForumRepository?
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

    private val _queues = MutableLiveData<List<Queue>>()
    val queues: LiveData<List<Queue>> get() = _queues

    private val _errorMessageQueues = MutableLiveData<String>()
    val errorMessageQueues: LiveData<String> get() = _errorMessageQueues

    private val _latestQueue = MutableLiveData<LatestQueue>()
    val latestQueue: LiveData<LatestQueue> get() = _latestQueue

    private val _errorMessageLatestQueue = MutableLiveData<String>()
    val errorMessageLatestQueue: LiveData<String> get() = _errorMessageLatestQueue

    private val _forums = MutableLiveData<List<Forum>>()
    val forums: LiveData<List<Forum>> get() = _forums

    private val _errorMessageForums = MutableLiveData<String>()
    val errorMessageForums: LiveData<String> get() = _errorMessageForums

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

    fun getQueues() {
        if (queueRepository == null) {
            Log.e("API_ERROR", "QueueRepository is null")
            _errorMessageQueues.postValue("QueueRepository is not initialized.")
            return
        }

        viewModelScope.launch {
            try {
                val responseQueues = queueRepository.getQueues()
                if (responseQueues.isSuccess) {
                    _queues.postValue(responseQueues.getOrNull()?.data)
                    Log.d("API_RESPONSE", "Queues fetched successfully: ${responseQueues.getOrNull()?.data}")
                } else {
                    _errorMessageQueues.postValue("An error occurred while fetching queues. Please try again.")
                    Log.e("API_ERROR", "Queue fetch failed: ${responseQueues.exceptionOrNull()}")
                }
            } catch (e: ConnectException) {
                _errorMessageQueues.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageQueues.postValue("An error occurred while fetching queues. Please try again.")
                Log.e("API_ERROR", "Queue Error: ${e.message}", e)
            }
        }
    }

    fun getLatestQueue(){
        if (queueRepository == null) {
            Log.e("API_ERROR", "QueueRepository is null")
            _errorMessageLatestQueue.postValue("QueueRepository is not initialized.")
            return
        }

        viewModelScope.launch {
            try {
                val responseQueues = queueRepository.getLatestQueue()
                if (responseQueues.isSuccess){
                    _latestQueue.postValue(responseQueues.getOrNull()?.data)
                    Log.d("API_RESPONSE", "Queues fetched successfully: ${responseQueues.getOrNull()?.data}")
                } else {
                    _errorMessageLatestQueue.postValue("An error occurred while fetching queues. Please try again.")
                    Log.e("API_ERROR", "Queue fetch failed: ${responseQueues.exceptionOrNull()}")
                }
            } catch (e: ConnectException) {
                _errorMessageLatestQueue.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception){
                _errorMessageLatestQueue.postValue("An error occurred while fetching queues. Please try again.")
                Log.e("API_ERROR", "Queue Error: ${e.message}", e)
            }
        }
    }

    fun getForums(){
        if (forumRepository == null){
            Log.e("API_ERROR", "ForumRepository is null")
            _errorMessageForums.postValue("ForumRepository is not initialized.")
            return
        }

        viewModelScope.launch {
            try {
                val responseForums = forumRepository.getForums()
                if (responseForums.isSuccess) {
                    _forums.postValue(responseForums.getOrNull()?.data)
                    Log.d("API_RESPONSE", "Queues fetched successfully: ${responseForums.getOrNull()?.data}")
                } else {
                    _errorMessageQueues.postValue("An error occurred while fetching queues. Please try again.")
                    Log.e("API_ERROR", "Queue fetch failed: ${responseForums.exceptionOrNull()}")
                }
            } catch (e: ConnectException) {
                _errorMessageQueues.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageQueues.postValue("An error occurred while fetching queues. Please try again.")
                Log.e("API_ERROR", "Queue Error: ${e.message}", e)
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
