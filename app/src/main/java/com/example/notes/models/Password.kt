package com.example.notes.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Password(
    @Json(name = "password")
    val password: String)
