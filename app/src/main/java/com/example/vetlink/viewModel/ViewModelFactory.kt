package com.example.vetlink.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.repository.ForumRepository
import com.example.vetlink.repository.PetRepository
import com.example.vetlink.repository.QueueRepository
import com.example.vetlink.repository.VeterinerRepository

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val petRepository: PetRepository? = null,
    private val queueRepository: QueueRepository? = null,
    private val veterinerRepository: VeterinerRepository? = null,
    private val forumRepository: ForumRepository? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginActivityViewModel::class.java) -> {
                LoginActivityViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(RegisterActivityViewModel::class.java) -> {
                RegisterActivityViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> {
                MainActivityViewModel(authRepository, veterinerRepository, queueRepository, forumRepository) as T
            }
            modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java) -> {
                ProfileFragmentViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(MenuActivityViewModel::class.java) -> {
                MenuActivityViewModel(authRepository, petRepository, queueRepository, veterinerRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}