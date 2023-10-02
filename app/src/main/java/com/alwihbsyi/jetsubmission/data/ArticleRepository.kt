package com.alwihbsyi.jetsubmission.data

import android.content.Context
import com.alwihbsyi.jetsubmission.data.entity.ArticleEntity
import com.alwihbsyi.jetsubmission.data.room.ArticleDao
import com.alwihbsyi.jetsubmission.model.FakeArticleDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ArticleRepository private constructor(
    private val articleDao: ArticleDao
) {

    fun getAllArticles(context: Context): Flow<List<ArticleEntity>> =
        flowOf(FakeArticleDataSource.getDummyData(context))

    fun getArticleById(context: Context, title: String): ArticleEntity {
        return FakeArticleDataSource.getDummyData(context).first {
            it.title == title
        }
    }

    suspend fun getSavedArticles(): Flow<List<ArticleEntity>> = flowOf(articleDao.getArticles())

    suspend fun getSavedArticleById(title: String): Flow<Int> = flowOf(articleDao.getArticleById(title))

    suspend fun insertArticle(article: ArticleEntity): Flow<Boolean> {
        articleDao.insertArticle(article)
        return flowOf(true)
    }

    suspend fun deleteArticle(article: ArticleEntity): Flow<Boolean> {
        articleDao.deleteArticle(article)
        return flowOf(true)
    }

    companion object {
        @Volatile
        private var instance: ArticleRepository? = null

        fun getInstance(
            articleDao: ArticleDao
        ): ArticleRepository =
            instance ?: synchronized(this) {
                instance ?: ArticleRepository(articleDao)
            }.also { instance = it }
    }
}