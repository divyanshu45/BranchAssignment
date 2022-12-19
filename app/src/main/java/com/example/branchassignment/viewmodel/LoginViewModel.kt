package com.example.branchassignment.viewmodel

import android.text.TextUtils
import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.branchassignment.models.UserRequest
import com.example.branchassignment.models.UserResponse
import com.example.branchassignment.repository.UserRepository
import com.example.branchassignment.utils.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){

    val userResponseLiveData: LiveData<NetworkResults<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateCredentials(emailAddress: String, password: String, isLogin: Boolean) : Pair<Boolean, String> {
        var result = Pair(true, "")
        if(!EMAIL_ADDRESS.matcher(emailAddress).matches()){
            result = Pair(false, "Email is invalid")
        }
        else if(!TextUtils.isEmpty(password) && password.length <= 5){
            result = Pair(false, "Password length should be greater than 5")
        }
        return result
    }
}