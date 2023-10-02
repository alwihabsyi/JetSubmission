package com.alwihbsyi.jetsubmission.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alwihbsyi.jetsubmission.data.ArticleRepository
import com.alwihbsyi.jetsubmission.ui.screen.bookmark.BookmarkViewModel
import com.alwihbsyi.jetsubmission.ui.screen.detail.DetailViewModel
import com.alwihbsyi.jetsubmission.ui.screen.home.HomeViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: ArticleRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> return HomeViewModel(repository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> return DetailViewModel(repository) as T
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> return BookmarkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}