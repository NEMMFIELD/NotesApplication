package com.example.notes.list.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteModel(val id:Long?, val author:String?, val noteText:String?) : Parcelable
