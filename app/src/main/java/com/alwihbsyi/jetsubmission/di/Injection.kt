package com.alwihbsyi.jetsubmission.di

import android.content.Context
import com.alwihbsyi.jetsubmission.data.ArticleRepository
import com.alwihbsyi.jetsubmission.data.room.ArticleDatabase

object Injection {

    fun provideRepository(context: Context): ArticleRepository {
        val database = ArticleDatabase.getInstance(context)
        val articleDao = database.userDao()

        return ArticleRepository.getInstance(articleDao)
    }

}