package com.hitzvera.storyapp.network

import com.hitzvera.storyapp.model.StoriesResponse
import com.hitzvera.storyapp.model.UserLoginResponse
import com.hitzvera.storyapp.model.UserRegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryAppApi {

    @FormUrlEncoded
    @POST("login")
    fun getLoginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun createAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserRegisterResponse>

    @GET("stories")
    fun getAllListStories(
        @Header("Authorization")
        authHeader: String
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") authHeader: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<UserRegisterResponse> // reuse



}