package com.example.todolistapp.di

import android.content.Context
import androidx.room.Room
import com.example.todolistapp.data.db.TodoDao
import com.example.todolistapp.data.db.TodoDatabase
import com.example.todolistapp.data.repository.TodoRepositoryImpl
import com.example.todolistapp.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase =
        Room.databaseBuilder(context, TodoDatabase::class.java, "todo.db").build()

    @Provides
    fun provideTodoDao(db: TodoDatabase) = db.todoDao()

    @Provides
    fun provideTodoRepository(dao: TodoDao): TodoRepository = TodoRepositoryImpl(dao)
}
