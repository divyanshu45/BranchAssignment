package com.example.branchassignment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.branchassignment.api.MessagesApi
import com.example.branchassignment.models.ChatMessageRequest
import com.example.branchassignment.models.UserMessagesItem
import com.example.branchassignment.utils.NetworkResults
import javax.inject.Inject

class MessagesRepository @Inject constructor(private val messagesApi: MessagesApi) {

    private val _userMessagesLiveData = MutableLiveData<NetworkResults<List<UserMessagesItem>>>()
    val userMessagesLiveData: LiveData<NetworkResults<List<UserMessagesItem>>>
        get() = _userMessagesLiveData

    private val _userChatResponseLiveData = MutableLiveData<NetworkResults<UserMessagesItem>>()
    val userChatResponseLiveData: LiveData<NetworkResults<UserMessagesItem>>
        get() = _userChatResponseLiveData

    suspend fun getMessages(){
        _userMessagesLiveData.postValue(NetworkResults.Loading())
        val response = messagesApi.getMessages()
        if(response.isSuccessful && response.body() != null){
            _userMessagesLiveData.postValue(NetworkResults.Success(response.body()!!))
        }else if(response.errorBody() != null){
            _userMessagesLiveData.postValue(NetworkResults.Error("Something went wrong"))
        }else{
            _userMessagesLiveData.postValue(NetworkResults.Error("Something went wrong"))
        }
    }

    suspend fun sendChat(chatMessageRequest: ChatMessageRequest){
        _userChatResponseLiveData.postValue(NetworkResults.Loading())
        val response = messagesApi.sendChat(chatMessageRequest)
        if(response.isSuccessful && response.body() != null){
            _userChatResponseLiveData.postValue(NetworkResults.Success(response.body()!!))
        }else if(response.errorBody() != null){
            _userChatResponseLiveData.postValue(NetworkResults.Error("Something went wrong"))
        }else{
            _userChatResponseLiveData.postValue(NetworkResults.Error("Something went wrong"))
        }
    }
}