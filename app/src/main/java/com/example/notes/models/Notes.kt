package com.example.notes.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Notes(
    @Json(name = "id")  // Primary key for Notes
    val id : Int,
    @Json(name = "title")
    var title : String,
    @Json(name = "description")
    val description : String,
    @Json(name = "userId")
    val userId : Int
):Serializable
