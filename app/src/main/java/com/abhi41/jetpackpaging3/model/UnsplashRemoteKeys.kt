package com.abhi41.jetpackpaging3.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhi41.jetpackpaging3.utils.Constants.UNSPLASH_REMOTE_KEYS_TABLE

/*
    the main purpose of this table is to store previous and next keys in local database,
    so that our remote mediator will know which page request next
 */

@Entity(tableName = UNSPLASH_REMOTE_KEYS_TABLE)
data class UnsplashRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)
