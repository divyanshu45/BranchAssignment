package com.example.branchassignment.api

import com.example.branchassignment.models.UserRequest
import com.example.branchassignment.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApi {

    @Headers("Content-Type: application/json")
    @POST("api/login")
    suspend fun login(@Body userRequest: UserRequest) : Response<UserResponse>
}