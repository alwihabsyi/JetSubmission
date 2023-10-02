package com.alwihbsyi.jetsubmission.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity("articles")
@Parcelize
data class ArticleEntity(
    @ColumnInfo("title")
    @PrimaryKey
    val title: String = "",

    @ColumnInfo("description")
    val description: String? = null,

    @ColumnInfo("photoUrl")
    val photoUrl: String? = null,

    @ColumnInfo("content")
    val content: String? = null,

    @ColumnInfo("url")
    val url: String? = null
): Parcelable
