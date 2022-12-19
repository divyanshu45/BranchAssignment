package com.example.branchassignment.models

data class MessageAdapterModel(
    val thread_id: Int,
    val messagesList: List<UserMessagesItem>,
    val latest_message: String,
    val user_id: String,
    val timestamp: String
)