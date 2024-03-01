package com.example.notes.network

import com.example.notes.network.ApiService.Companion.retrofit
import com.example.notes.network.Service.Companion.retrofits

object ApiClient {
    //this is for json response
    val apiClient:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    //this is for initializing interface of handling string returning response from api call
    val apis:Service by lazy {
        retrofits.create(Service::class.java)
    }
}