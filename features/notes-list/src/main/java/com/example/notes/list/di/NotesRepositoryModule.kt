package com.example.notes.list.di

import com.example.database.NoteEntity
import com.example.notes.api.NotesApi
import com.example.notes.list.data.NotesRepositoryImpl
import com.example.notes.list.domain.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotesRepositoryModule {

    @Provides
    @Singleton
    fun provideRandomNotesRepository(notesApi: NotesApi,notesBox: Box<NoteEntity>): NotesRepository = NotesRepositoryImpl(notesApi, notesBox)
}
