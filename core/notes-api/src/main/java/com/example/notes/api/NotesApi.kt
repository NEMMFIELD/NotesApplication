package com.example.notes.api

import com.example.notes.api.model.NoteResponse
import retrofit2.http.GET

interface NotesApi {
    @GET("api/random")
    suspend fun getNote(): List<NoteResponse>
}
