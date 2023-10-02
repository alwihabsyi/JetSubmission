package com.alwihbsyi.jetsubmission.model

import android.content.Context
import com.alwihbsyi.jetsubmission.data.entity.ArticleEntity
import com.google.gson.Gson

object FakeArticleDataSource {

    fun getDummyData(context: Context): ArrayList<ArticleEntity> {
        val content = context.assets.open("jsonartikel.json").bufferedReader().use { it.readText() }
        val pagingData = Gson().fromJson(content, Response::class.java)
        val articles = arrayListOf<ArticleEntity>()

        pagingData.articles?.forEach { article ->
            article?.let {
                articles.add(
                    ArticleEntity(
                        it.title!!,
                        it.description,
                        it.urlToImage,
                        it.content,
                        it.url
                    )
                )
            }
        }

        return articles
    }

}