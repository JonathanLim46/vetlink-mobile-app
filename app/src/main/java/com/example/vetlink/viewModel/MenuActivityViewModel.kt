package com.example.vetlink.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetlink.data.model.pets.Pet
import com.example.vetlink.data.model.pets.PetAddResponse
import com.example.vetlink.data.model.pets.PetDetails
import com.example.vetlink.data.model.pets.PetType
import com.example.vetlink.data.model.queue.Queue
import com.example.vetlink.data.model.user.User
import com.example.vetlink.data.model.veteriner.Veteriner
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.repository.ForumRepository
import com.example.vetlink.repository.PetRepository
import com.example.vetlink.repository.QueueRepository
import com.example.vetlink.repository.VeterinerRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.ConnectException

class MenuActivityViewModel(
    private val authRepository: AuthRepository,
    private val petRepository: PetRepository?,
    private val queueRepository: QueueRepository?,
    private val veterinerRepository: VeterinerRepository?,
    private val forumRepository: ForumRepository?
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

    private val _petDetail = MutableLiveData<PetDetails>()
    val petDetail: LiveData<PetDetails> get() = _petDetail

    private val _updateMessagePetUpdate = MutableLiveData<String>()
    val updateMessagePetUpdate: LiveData<String> get() = _updateMessagePetUpdate

    private val _errorMessagePetUpdate = MutableLiveData<String>()
    val errorMessagePetUpdate: LiveData<String> get() = _errorMessagePetUpdate

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

    private val _veterinerDetail = MutableLiveData<Veteriner>()
    val veterinerDetail: LiveData<Veteriner> get() = _veterinerDetail

    private val _errorMessageVeterinerDetail = MutableLiveData<String>()
    val errorMessageVeterinerDetail: LiveData<String> get() = _errorMessageVeterinerDetail

    val addQueueResponse = MutableLiveData<Int>()

    val messageAddForum = MutableLiveData<Int>()
    val errorMessageAddForum = MutableLiveData<String>()

    private val _errorMessageAddQueue = MutableLiveData<String>()

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
                _errorMessagePet.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessagePet.postValue("An error occurred. Please try again.")
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
                _errorMessagePet.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessagePet.postValue("An error occurred. Please try again.")
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
                _errorMessagePetTypeBreed.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessagePetTypeBreed.postValue("An error occurred. Please try again.")
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
                    Toast.makeText(null, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }catch (e: ConnectException){
                _errorMessagePetDetail.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            }catch (e: Exception){
                _errorMessagePetDetail.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Delete Pet error: ${e.message}")
            }
        }
    }

    fun updatePet(
        id: Int,
        params: MutableMap<String, RequestBody>,
        photoPart: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            try {
                val response = petRepository?.editPet(id, params, photoPart)
                if (response!!.isSuccess){
                    _updateMessagePetUpdate.postValue("Update success!")
                }
            }catch (e: ConnectException){
                _errorMessagePetUpdate.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            }catch (e: Exception){

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
                _errorMessageDeletePet.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageDeletePet.postValue("An error occurred. Please try again.")
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

    fun getDetailClinic(id: Int){
        viewModelScope.launch {
            try {
                val response = veterinerRepository?.getVeteriner(id)
                if (response!!.isSuccess){
                    _veterinerDetail.postValue(response.getOrNull()?.data)
                    Log.d("API_RESPONSE", "Veteriner detail fetched successfully: ${response.getOrNull()?.data}")
                }
            }catch (e: ConnectException) {
                _errorMessageVeterinerDetail.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            }catch (e: Exception){
                _errorMessageVeterinerDetail.postValue("An error occurred. Please try again.")
                Log.d("exception vet detail", e.message.toString())
            }
        }
    }

    fun addQueue(petId: Int, veterinerId: Int, date: String){
        viewModelScope.launch {
            try {
                val response = queueRepository?.addQueue(petId, veterinerId, date)
                if (response!!.isSuccess && response.getOrNull()?.status == 201){
                    addQueueResponse.postValue(response.getOrNull()?.status)
                    Log.d("API_RESPONSE", "Queue added successfully: ${response.getOrNull()?.data}")
                }
                else{
                    addQueueResponse.postValue(response.getOrNull()?.status)
                    Log.e("API_ERROR", "Add Queue failed: ${response?.exceptionOrNull()}")
                }
            }catch (e: ConnectException){
                _errorMessageQueues.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            }
            catch (e: Exception){
                _errorMessageQueues.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Queue Error: ${e.message}", e)
            }
        }

    }

    fun addForum(
        params: MutableMap<String, RequestBody>,
        photoPart: MultipartBody.Part?
    ){
        viewModelScope.launch {
            val response = forumRepository?.addForum(params, photoPart)
            if (response!!.isSuccess){
                messageAddForum.postValue(response.getOrNull()?.status)
                Log.d("API_RESPONSE", "Forum added successfully: ${response.getOrNull()?.data}")
            } else {
                errorMessageAddForum.postValue("Failed to add pet. Please try again.")
                Log.e("API_ERROR", "Add Forum failed: ${response?.exceptionOrNull()}")
            }
        }
    }

}