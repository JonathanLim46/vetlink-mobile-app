package com.example.vetlink.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vetlink.data.model.pets.Pet
import com.example.vetlink.data.model.pets.PetAddResponse
import com.example.vetlink.data.model.pets.PetType
import com.example.vetlink.data.model.queue.Queue
import com.example.vetlink.data.model.user.User
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.repository.PetRepository
import com.example.vetlink.repository.QueueRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.net.ConnectException

class MenuActivityViewModel(
    private val authRepository: AuthRepository,
    private val petRepository: PetRepository?,
    private val queueRepository: QueueRepository?
): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _errorMessageUser = MutableLiveData<String>()
    val errorMessageUser: LiveData<String> get() = _errorMessageUser

    val addPetResponse = MutableLiveData<PetAddResponse?>()
    val errorMessageAddPet = MutableLiveData<String>()

    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> get() = _pets

    private val _errorMessagePet = MutableLiveData<String>()
    val errorMessagePet: LiveData<String> get() = _errorMessagePet

    private val _petDetail = MutableLiveData<Pet>()
    val petDetail: LiveData<Pet> get() = _petDetail

    private val _errorMessagePetDetail = MutableLiveData<String>()
    val errorMessagePetDetail: LiveData<String> get() = _errorMessagePetDetail

    private val _petTypes = MutableLiveData<List<PetType>>()
    val petTypes: LiveData<List<PetType>> get() = _petTypes

    private val _errorMessagePetTypeBreed = MutableLiveData<String>()
    val errorMessagePetTypeBreed: LiveData<String> get() = _errorMessagePetTypeBreed

    private val _queues = MutableLiveData<List<Queue>>()
    val queues: LiveData<List<Queue>> get() = _queues

    private val _errorMessageQueues = MutableLiveData<String>()
    val errorMessageQueues: LiveData<String> get() = _errorMessageQueues

    private val _deletePetMessage = MutableLiveData<String>()
    val deletePetMessage: LiveData<String> get() = _deletePetMessage
    val _errorMessageDeletePet = MutableLiveData<String>()

    fun addPet(
        name: String,
        type: String,
        file: MultipartBody.Part,
        breed: String,
        age: String,
        weight: String,
        gender: String,
        note: String? = null
    ){
        viewModelScope.launch {
            try {
                val response = petRepository?.addPet(name, type, file, breed, age, weight, gender, note)
                if (response!!.isSuccess){
                    addPetResponse.postValue(response.getOrNull())
                    Log.d("API_RESPONSE", "Pet added successfully: $addPetResponse")
                } else {
                    errorMessageAddPet.postValue("Failed to add pet. Please try again.")
                    Log.e("API_ERROR", "Add Pet failed: ${response?.exceptionOrNull()}")
                }
            }catch (e: ConnectException) {
                _errorMessageUser.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageUser.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Pet Add error: ${e.message}")
            }
        }
    }


    fun getPets(){
        viewModelScope.launch {
            try {
                val responsePets = petRepository?.getPets()
                if (responsePets!!.isSuccess){
                    _pets.postValue(responsePets.getOrNull()?.data)
                    Log.d("API_RESPONSE", "Pets fetched successfully: ${responsePets.getOrNull()?.data}")
                }else{
                    _errorMessagePet.postValue("An error occurred. Please try again.")
                }
            }catch (e: ConnectException) {
                _errorMessageUser.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageUser.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Pet error: ${e.message}")
            }
        }
    }

    fun getPetTypeBreed(){
        viewModelScope.launch {
            try {
                val response = petRepository?.getPetTypes()
                if (response != null && response.isSuccess) {
                    _petTypes.postValue(response.getOrNull()?.data)
                    Log.d("API_RESPONSE", "Pet types and breeds fetched successfully: ${response.getOrNull()?.data}")
                } else {
                    _errorMessagePetTypeBreed.postValue("An error occurred. Please try again.")
                    Log.e("API_ERROR", "Fetch failed: ${response?.exceptionOrNull()}")
                }
            }catch (e: ConnectException) {
                _errorMessageUser.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageUser.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Pet error: ${e.message}")
            }
        }
    }

    fun getPetDetails(petId: Int) {
        viewModelScope.launch {
            try {
                val response = petRepository?.getPetDetails(petId)
                if (response!!.isSuccess){
                    _petDetail.postValue(response.getOrNull()?.data)
                    Log.d("API_RESPONSE", "Pet details fetched successfully: ${response.getOrNull()?.data}")
                }else{
                    _errorMessagePetDetail.postValue("An error occurred. Please try again.")
                }
            }catch (e: ConnectException){
                _errorMessageUser.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            }catch (e: Exception){
                _errorMessageUser.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Delete Pet error: ${e.message}")
            }
        }
    }

    fun deletePet(petId: Int) {
        viewModelScope.launch {
            try {
                val response = petRepository?.deletePet(petId)
                if (response!!.isSuccess) {
                    _deletePetMessage.postValue("Pet deleted successfully")
                } else {
                    _errorMessageDeletePet.postValue("An error occurred. Please try again.")
                }
            }catch (e: ConnectException) {
                _errorMessageUser.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageUser.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Delete Pet error: ${e.message}")
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


}