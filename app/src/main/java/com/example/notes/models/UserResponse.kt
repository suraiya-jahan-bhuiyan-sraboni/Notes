package com.example.notes.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id")
    var id : Int ,
    @Json(name = "name")
    var name : String ,
    @Json(name = "username")
    var username : String ,
    @Json(name = "password")
    var password : String
)
