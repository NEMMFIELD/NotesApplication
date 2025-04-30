package com.example.notes.edit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditNoteModel(
    val id: Long?, val author: String?, val noteText: String?
) : Parcelable
