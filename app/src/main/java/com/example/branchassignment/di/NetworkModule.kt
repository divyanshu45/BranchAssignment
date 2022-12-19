package com.example.branchassignment.di

import com.example.branchassignment.api.AuthInterceptor
import com.example.branchassignment.api.MessagesApi
import com.example.branchassignment.utils.Constants.BASE_URL
import com.example.branchassignment.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder() : Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserApi(retrofitBuilder: Retrofit.Builder) : UserApi {
        return retrofitBuilder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providesMessagesApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): MessagesApi {
        return retrofitBuilder.client(okHttpClient).build().create(MessagesApi::class.java)
    }
}