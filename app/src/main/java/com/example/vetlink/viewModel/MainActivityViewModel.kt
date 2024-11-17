package com.example.vetlink.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetlink.Resource
import com.example.vetlink.data.model.comment.Comment
import com.example.vetlink.data.model.forums.Forum
import com.example.vetlink.data.model.queue.LatestQueue
import com.example.vetlink.data.model.queue.Queue
import com.example.vetlink.data.model.user.User
import com.example.vetlink.data.model.veteriner.Veteriner
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.repository.ForumRepository
import com.example.vetlink.repository.QueueRepository
import com.example.vetlink.repository.VeterinerRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.ConnectException

class MainActivityViewModel(
    private val authRepository: AuthRepository,
    private val veterinerRepository: VeterinerRepository?,
    private val queueRepository: QueueRepository?,
    private val forumRepository: ForumRepository?
): ViewModel() {

    var cityNow: String? = null

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _errorMessageUser = MutableLiveData<String>()
    val errorMessageUser: LiveData<String> get() = _errorMessageUser

    private val _getUserHome = MutableLiveData<Resource<User>>()
    val getUserHome: LiveData<Resource<User>> get() = _getUserHome

    private val _errorMessageUserHome = MutableLiveData<String>()
    val errorMessageUserHome: LiveData<String> get() = _errorMessageUserHome

//    private val _pets = MutableLiveData<List<Pet>>()
//    val pets: LiveData<List<Pet>> get() = _pets

//    private val _errorMessagePet = MutableLiveData<String>()
//    val errorMessagePet: LiveData<String> get() = _errorMessagePet

    private val _veteriners = MutableLiveData<Resource<List<Veteriner>>>()
    val veteriners: LiveData<Resource<List<Veteriner>>> get() = _veteriners

    private val _errorMessageVeteriner = MutableLiveData<String>()
    val errorMessageVeteriner: LiveData<String> get() = _errorMessageVeteriner

    private val _queues = MutableLiveData<Resource<List<Queue>>>()
    val queues: LiveData<Resource<List<Queue>>> get() = _queues

    private val _errorMessageQueues = MutableLiveData<String>()
    val errorMessageQueues: LiveData<String> get() = _errorMessageQueues

    private val _latestQueue = MutableLiveData<Resource<LatestQueue>>()
    val latestQueue: LiveData<Resource<LatestQueue>> get() = _latestQueue

    private val _errorMessageLatestQueue = MutableLiveData<String>()
    val errorMessageLatestQueue: LiveData<String> get() = _errorMessageLatestQueue

    private val _forums = MutableLiveData<List<Forum>>()
    val forums: LiveData<List<Forum>> get() = _forums

    private val _errorMessageForums = MutableLiveData<String>()
    val errorMessageForums: LiveData<String> get() = _errorMessageForums

    private val _forum = MutableLiveData<Forum>()
    val forum: LiveData<Forum> get() = _forum

    private val _forumsComments = MutableLiveData<List<Comment>>()
    val forumsComments: LiveData<List<Comment>> get() = _forumsComments

    private val _errorMessageForumsComments = MutableLiveData<String>()
    val errorMessageForumsComments: LiveData<String> get() = _errorMessageForumsComments

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> get() = _logoutSuccess



    val deleteForumStatus = MutableLiveData<Int>()
    val updateForumStatusResponse = MutableLiveData<Int>()
    val errorForumStatus = MutableLiveData<String>()
    val addCommentStatus = MutableLiveData<Int>()

    fun fetchUser() {
        viewModelScope.launch {

            _getUserHome.postValue(Resource.Loading())

            try {
                val responseUser = authRepository.getProfile()
                _getUserHome.postValue(Resource.Success(responseUser.data))
                _user.postValue(responseUser.data)
            } catch (e: ConnectException) {
                _errorMessageUserHome.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
                _getUserHome.postValue(Resource.Error("An error occurred while fetching queues.", null))
            } catch (e: Exception) {
                _errorMessageUserHome.postValue("An error occurred. Please try again.")
                 Log.e("API_ERROR", "Fetch user error: ${e.message}")
                _getUserHome.postValue(Resource.Error("Fetch user error: ${e.message}", null))
            }
        }
    }

    fun getVeteriners() {

        _veteriners.postValue(Resource.Loading())

        viewModelScope.launch {
            try {
                val responseVeteriners = veterinerRepository?.getVeteriners()
                if (responseVeteriners?.isSuccess == true) {
                    val veterinerList = responseVeteriners.getOrNull()?.data ?: emptyList()
                    _veteriners.postValue(Resource.Success(veterinerList))
                    Log.d("API_RESPONSE", "Veteriners fetched successfully: ${veterinerList.size}")
                } else {
                    _errorMessageVeteriner.postValue("Failed to fetch veteriners.")
                    Log.e("API_ERROR", "Veteriners fetch error: ${responseVeteriners?.exceptionOrNull()?.message}")
                    _veteriners.postValue(Resource.Error("Failed to fetch veteriners.", null))
                }
            }catch (e: ConnectException) {
                _errorMessageUser.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
                _veteriners.postValue(Resource.Error("Unable to connect to the server. Please check your internet connection.", null))
            } catch (e: Exception) {
                _errorMessageUser.postValue("An error occurred. Please try again.")
                Log.e("API_ERROR", "Fetch user error: ${e.message}")
                _veteriners.postValue(Resource.Error("An error occurred. Please try again", null))
            }
        }
    }

    fun getQueues() {
        if (queueRepository == null) {
            Log.e("API_ERROR", "QueueRepository is null")
            _errorMessageQueues.postValue("QueueRepository is not initialized.")
            return
        }

        _queues.postValue(Resource.Loading())

        viewModelScope.launch {
            try {
                val responseQueues = queueRepository.getQueues()
                if (responseQueues.isSuccess) {
                    _queues.postValue(Resource.Success(responseQueues.getOrNull()?.data ?: emptyList()))
                    Log.d("API_RESPONSE", "Queues fetched successfully: ${responseQueues.getOrNull()?.data}")
                } else {
                    _errorMessageQueues.postValue("An error occurred while fetching queues. Please try again.")
                    Log.e("API_ERROR", "Queue fetch failed: ${responseQueues.exceptionOrNull()}")
                    _queues.postValue(Resource.Error("An error occurred while fetching queues.", null))
                }
            } catch (e: ConnectException) {
                _errorMessageQueues.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
                _queues.postValue(Resource.Error("Network error: ${e.message}", null))
            } catch (e: Exception) {
                _errorMessageQueues.postValue("An error occurred while fetching queues. Please try again.")
                Log.e("API_ERROR", "Queue Error: ${e.message}", e)
                _queues.postValue(Resource.Error("An error occurred: ${e.message}", null))
            }
        }
    }

    fun getLatestQueue(){
        if (queueRepository == null) {
            Log.e("API_ERROR", "QueueRepository is null")
            _errorMessageLatestQueue.postValue("QueueRepository is not initialized.")
            return
        }

        _latestQueue.postValue(Resource.Loading())

        viewModelScope.launch {
            try {
                val responseQueues = queueRepository.getLatestQueue()
                if (responseQueues.isSuccess){
                    _latestQueue.postValue(responseQueues.getOrNull()
                        ?.let { Resource.Success(it.data) })
                    Log.d("API_RESPONSE", "Queues fetched successfully: ${responseQueues.getOrNull()?.data}")
                } else {
                    _errorMessageLatestQueue.postValue("An error occurred while fetching queues. Please try again.")
                    Log.e("API_ERROR", "Queue fetch failed: ${responseQueues.exceptionOrNull()}")
                    _latestQueue.postValue(Resource.Error("An error occurred while fetching queues. Please try again.", null))
                }
            } catch (e: ConnectException) {
                _errorMessageLatestQueue.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
                _latestQueue.postValue(Resource.Error("Unable to connect to the server. Please check your internet connection.", null))
            } catch (e: Exception){
                _errorMessageLatestQueue.postValue("An error occurred while fetching queues. Please try again.")
                Log.e("API_ERROR", "Queue Error: ${e.message}", e)
                _latestQueue.postValue(Resource.Error("\"An error occurred while fetching queues. Please try again. ", null))
            }
        }
    }

    fun getForums(){
        viewModelScope.launch {
            try {
                val responseForums = forumRepository?.getForums()
                if (responseForums!!.isSuccess) {
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

    fun getForum(id: Int){
        viewModelScope.launch {
            try {
                val response = forumRepository?.getForum(id)
                if (response != null && response.getOrNull()?.status == 200) {
                    _forum.postValue(response.getOrNull()?.data)
                }else{
                    _errorMessageForums.postValue("An error occurred. Please try again.")
                }
            }catch (e: ConnectException) {
                _errorMessageQueues.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageQueues.postValue("An error occurred while fetching queues. Please try again.")
                Log.e("API_ERROR", "Queue Error: ${e.message}", e)
            }
        }
    }

    fun deleteForum(id: Int){
        viewModelScope.launch {
            val response = forumRepository?.deleteForum(id)
            if (response != null) {
                deleteForumStatus.postValue(response.getOrNull()?.status)
            }
        }
    }

    fun updateForumStatus(id: Int){
        viewModelScope.launch {
            try {
                val response = forumRepository?.updateForumStatus(id)
                if (response != null) {
                    updateForumStatusResponse.postValue(response.getOrNull()?.status)
                }
            }catch (e: ConnectException){
                errorForumStatus.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            }catch (e: Exception){
                errorForumStatus.postValue("An error occurred while fetching queues. Please try again.")
                Log.e("API_ERROR", "Edit Forum Status Error: ${e.message}", e)
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

    fun getComments(postId: Int) {
        viewModelScope.launch {
            try {
                val response = forumRepository?.getForumComments(postId)
                if (response != null) {
                    _forumsComments.postValue(response.getOrNull()?.data)
                }else{
                    _errorMessageForumsComments.postValue("An error occurred. Please try again.")
                }
            } catch (e: ConnectException) {
                _errorMessageForumsComments.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageForums.postValue("An error occurred while fetching queues. Please try again.")
                Log.e("API_ERROR", "Comments Error: ${e.message}", e)
            }
        }
    }

    fun addComment(postId: Int, comment: String){
        viewModelScope.launch {
            try {
                val response = forumRepository?.addForumComment(postId, comment)
                if (response != null) {
                    if (response.getOrNull()?.status == 201){
                        addCommentStatus.postValue(response.getOrNull()!!.status)
                    } else{
                        _errorMessageForumsComments.postValue("An error occurred. Please try again.")
                    }
                }else{
                    _errorMessageForumsComments.postValue("An error occurred. Please try again.")
                }
            }catch (e: ConnectException) {
                _errorMessageForumsComments.postValue("Unable to connect to the server. Please check your internet connection.")
                Log.e("API_ERROR", "Network error: ${e.message}")
            } catch (e: Exception) {
                _errorMessageForums.postValue("An error occurred while fetching queues. Please try again.")
                Log.e("API_ERROR", "Create Comments Error: ${e.message}", e)
            }
        }
    }

}
