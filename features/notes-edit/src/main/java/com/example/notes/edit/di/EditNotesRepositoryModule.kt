package com.example.notes.edit.di

import com.example.database.NoteEntity
import com.example.notes.api.NotesApi
import com.example.notes.edit.data.EditNotesRepositoryImpl
import com.example.notes.edit.domain.EditNotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EditNotesRepositoryModule {
    @Provides
    @Singleton
    fun provideRandomNotesRepository(notesApi: NotesApi,notesBox: Box<NoteEntity>): EditNotesRepository = EditNotesRepositoryImpl(notesApi, notesBox)
}
