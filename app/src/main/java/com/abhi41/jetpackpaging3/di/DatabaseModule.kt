package com.abhi41.jetpackpaging3.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.abhi41.jetpackpaging3.data.database.UnsplashDatabase
import com.abhi41.jetpackpaging3.utils.Constants
import com.abhi41.jetpackpaging3.utils.Constants.UNSPLASH_Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): UnsplashDatabase{
        return Room.databaseBuilder(
            context,
            UnsplashDatabase::class.java,
            UNSPLASH_Database
        ).build()
    }

}