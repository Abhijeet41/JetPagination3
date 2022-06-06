package com.abhi41.jetpackpaging3.model

import androidx.room.Embedded
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("links")
    @Embedded //room doesn't know how to store data in database that's why we used this annotation
    val userLinks: UserLinks?,
    val username: String?
)
