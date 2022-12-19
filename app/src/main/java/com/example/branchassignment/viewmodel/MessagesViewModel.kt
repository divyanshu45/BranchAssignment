package com.example.branchassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.branchassignment.models.ChatMessageRequest
import com.example.branchassignment.models.UserMessagesItem
import com.example.branchassignment.repository.MessagesRepository
import com.example.branchassignment.repository.UserRepository
import com.example.branchassignment.utils.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(private val messagesRepository: MessagesRepository) : ViewModel(){

    val userMessagesLiveData: LiveData<NetworkResults<List<UserMessagesItem>>>
        get() = messagesRepository.userMessagesLiveData

    val userChatResponseLiveData: LiveData<NetworkResults<UserMessagesItem>>
        get() = messagesRepository.userChatResponseLiveData

    fun getMessages(){
        viewModelScope.launch {
            messagesRepository.getMessages()
        }
    }

    fun sendChat(chatMessageRequest: ChatMessageRequest){
        viewModelScope.launch {
            messagesRepository.sendChat(chatMessageRequest)
        }
    }
}