package com.example.notes.network

import com.example.notes.models.NewNote
import com.example.notes.models.Notes
import com.example.notes.models.UserLoginResponse
import com.example.notes.models.UserResponse
import com.example.notes.utils.baseUrl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @POST("/api/users/loginUser")
    fun loginUser(@Body userLoginResponse: UserLoginResponse): Call<UserResponse>
    @POST("/api/users/NotesList")
    fun noteList(@Query("userId") userId:Int):Call<List<Notes>>
    @POST("/api/users/createNote")
    fun createNote(@Body notes: NewNote):Call<Notes>
    @PUT("/api/users/updateSingleNotes")
    fun updateNote(@Body notes: Notes):Call<Notes>

    companion object{
        private val moshi= Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}