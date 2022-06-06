package com.abhi41.jetpackpaging3.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhi41.jetpackpaging3.utils.Constants.UNSPLASHED_IMAGE_TABLE
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = UNSPLASHED_IMAGE_TABLE)
data class UnsplashImage(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @Embedded
    val urls: Urls?,
    val likes: Int?,
    @Embedded
    val user: User?,
)
