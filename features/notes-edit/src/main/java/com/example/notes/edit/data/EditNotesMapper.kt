package com.example.notes.edit.data

import com.example.database.NoteEntity
import com.example.notes.api.model.NoteResponse

class EditNotesMapper {
    companion object {
        fun mapFromDtoToModel(randomNoteDto: NoteResponse): EditNoteModel {
            return EditNoteModel(author = randomNoteDto.author, noteText = randomNoteDto.quote, id = 0)
        }

        fun mapFromDtoToEntity(randomNoteDto: NoteResponse): NoteEntity {
            return NoteEntity(author = randomNoteDto.author, noteText = randomNoteDto.quote, id = 0)
        }

        fun mapFromDbToModel(randomNoteDb: NoteEntity): EditNoteModel {
            return EditNoteModel(author = randomNoteDb.author, noteText = randomNoteDb.noteText, id = randomNoteDb.id)
        }
    }
}
