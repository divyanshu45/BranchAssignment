package com.example.branchassignment.api

import com.example.branchassignment.models.ChatMessageRequest
import com.example.branchassignment.models.UserMessagesItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MessagesApi {

    @Headers("Content-Type: application/json")
    @GET("api/messages")
    suspend fun getMessages() : Response<List<UserMessagesItem>>

    @Headers("Content-Type: application/json")
    @POST("api/messages")
    suspend fun sendChat(@Body chatMessageRequest: ChatMessageRequest) : Response<UserMessagesItem>
}