package com.example.branchassignment.models

data class UserMessagesItem(
    val agent_id: Any,
    val body: String,
    val id: Int,
    val thread_id: Int,
    val timestamp: String,
    val user_id: String
)