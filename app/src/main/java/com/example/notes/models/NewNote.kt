package com.example.notes.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewNote(    @Json(name = "title")
                       var title : String,
                       @Json(name = "description")
                       val description : String,
                       @Json(name = "userId")
                       val userId : Int)
