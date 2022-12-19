package com.example.branchassignment.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.branchassignment.api.UserApi
import com.example.branchassignment.models.UserRequest
import com.example.branchassignment.models.UserResponse
import com.example.branchassignment.utils.Constants.TAG
import com.example.branchassignment.utils.NetworkResults
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _userResponseLiveData = MutableLiveData<NetworkResults<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResults<UserResponse>>
        get() = _userResponseLiveData

    suspend fun loginUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResults.Loading())
        val response = userApi.login(userRequest)
        if(response.isSuccessful && response.body() != null){
            _userResponseLiveData.postValue(NetworkResults.Success(response.body()!!))
        }else if(response.errorBody() != null){
            _userResponseLiveData.postValue(NetworkResults.Error("Something went wrong"))
        }else{
            _userResponseLiveData.postValue(NetworkResults.Error("Something went wrong"))
        }
        Log.d(TAG, response.body().toString())
    }
}