package com.alwihbsyi.jetsubmission.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alwihbsyi.jetsubmission.data.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
abstract class ArticleDatabase: RoomDatabase(){

    abstract fun userDao(): ArticleDao

    companion object {
        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase {
            INSTANCE ?: synchronized(ArticleDatabase::class.java) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java, "article_database"
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE as ArticleDatabase
        }
    }

}