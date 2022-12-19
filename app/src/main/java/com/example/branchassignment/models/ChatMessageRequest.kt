package com.example.branchassignment.models

data class ChatMessageRequest(
    val thread_id: Int,
    val body: String
)