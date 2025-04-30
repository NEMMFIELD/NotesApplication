package com.example.notesapplication.di

import android.content.Context
import com.example.database.MyObjectBox
import com.example.database.NoteEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ObjectBoxModule {

    @Provides
    @Singleton
    fun provideBoxStore(@ApplicationContext context: Context): BoxStore {
        return MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }

    @Provides
    fun provideNoteBox(boxStore: BoxStore): Box<NoteEntity> {
        return boxStore.boxFor(NoteEntity::class.java)
    }
}
