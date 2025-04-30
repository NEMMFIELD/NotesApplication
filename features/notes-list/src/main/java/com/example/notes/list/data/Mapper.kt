package com.example.notes.list.data

import com.example.database.NoteEntity
import com.example.notes.api.model.NoteResponse

class Mapper {
    companion object {
        fun mapFromDtoToModel(randomNoteDto: NoteResponse): NoteModel {
            return NoteModel(author = randomNoteDto.author, noteText = randomNoteDto.quote, id = 0)
        }

        fun mapFromDtoToEntity(randomNoteDto: NoteResponse): NoteEntity {
            return NoteEntity(author = randomNoteDto.author, noteText = randomNoteDto.quote, id = 0)
        }

        fun mapFromDbToModel(randomNoteDb: NoteEntity): NoteModel {
            return NoteModel(
                author = randomNoteDb.author,
                noteText = randomNoteDb.noteText,
                id = randomNoteDb.id
            )
        }

        fun mapFromModelToDb(noteModel: NoteModel): NoteEntity {
            return NoteEntity(
                id = noteModel.id,
                author = noteModel.author.toString(),
                noteText = noteModel.noteText.toString()
            )
        }
    }
}
