package com.example.notes.network

import com.example.notes.models.Password
import com.example.notes.models.UserRegister
import com.example.notes.utils.baseUrl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface Service {
    @POST("/api/users/RegisterUser")
    fun signUpUser(@Body userRegister: UserRegister): Call<String>
    @PUT("/api/users/resetPassword")
    fun resetPassword(@Query("userName") username:String,@Body password:Password):Call<String>
    @DELETE("/api/users/deleteUser")
    fun deleteUser(@Query("id") id:Int):Call<String>
    @POST("/api/users/DeleteAllNotes")
    fun deleteUserAllNotes(@Query("userId") userId:Int):Call<String>
    @POST("/api/users/DeleteSingleNotes")
    fun deleteSingleNotes(@Query("id") id : Int, @Query("userId") userId : Int) : Call<String>
    companion object{
        private val moshi= Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofits: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl).addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}