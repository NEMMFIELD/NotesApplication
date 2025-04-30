package com.example.notes.api.model

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse(
    @Json(name = "q") val quote: String,
    @Json(name = "a") val author: String
)
