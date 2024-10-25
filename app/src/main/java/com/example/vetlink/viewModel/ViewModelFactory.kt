package com.example.vetlink.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vetlink.repository.AuthRepository

class ViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginActivityViewModel::class.java) -> {
                LoginActivityViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(RegisterActivityViewModel::class.java) -> {
                RegisterActivityViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> {
                MainActivityViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java) -> {
                ProfileFragmentViewModel(authRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}