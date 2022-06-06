package com.abhi41.jetpackpaging3.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abhi41.jetpackpaging3.data.database.dao.UnsplashImageDao
import com.abhi41.jetpackpaging3.data.database.dao.UnsplashRemoteKeyDao
import com.abhi41.jetpackpaging3.model.UnsplashImage
import com.abhi41.jetpackpaging3.model.UnsplashRemoteKeys

@Database(entities = [UnsplashImage::class, UnsplashRemoteKeys::class], version = 1)
abstract class UnsplashDatabase: RoomDatabase() {

    abstract fun unsplashImageDao(): UnsplashImageDao
    abstract fun unsplashRemoteKeysDao(): UnsplashRemoteKeyDao

}