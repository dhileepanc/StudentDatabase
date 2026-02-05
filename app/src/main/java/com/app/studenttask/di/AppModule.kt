package com.app.studenttask.di

import android.content.Context
import com.app.studenttask.data.local.StudentDatabaseHelper
import com.app.studenttask.repository.StudentRepository
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
    fun provideDatabaseHelper(@ApplicationContext context: Context): StudentDatabaseHelper {
        return StudentDatabaseHelper(context)
    }

    @Provides
    @Singleton
    fun provideStudentRepository(dbHelper: StudentDatabaseHelper): StudentRepository {
        return StudentRepository(dbHelper)
    }
}
