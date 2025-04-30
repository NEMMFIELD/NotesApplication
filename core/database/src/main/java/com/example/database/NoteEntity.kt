package com.example.database

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class NoteEntity (
    @Id
    var id: Long? = 0,
    var noteText: String = "",
    var author: String = ""
)
