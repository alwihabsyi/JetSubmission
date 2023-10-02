package com.alwihbsyi.jetsubmission.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alwihbsyi.jetsubmission.data.entity.ArticleEntity

@Dao
interface ArticleDao {

    @Query("SELECT * from articles")
    suspend fun getArticles(): List<ArticleEntity>

    @Query("SELECT COUNT(*) from articles where title = :title")
    suspend fun getArticleById(title: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(article: ArticleEntity)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

}