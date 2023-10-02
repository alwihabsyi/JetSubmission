package com.alwihbsyi.jetsubmission.ui.screen.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alwihbsyi.jetsubmission.data.ArticleRepository
import com.alwihbsyi.jetsubmission.data.entity.ArticleEntity
import com.alwihbsyi.jetsubmission.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: ArticleRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<ArticleEntity>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<ArticleEntity>> get() = _uiState

    private val _userSaved: MutableStateFlow<UiState<Int>> = MutableStateFlow(UiState.Loading)
    val userSaved: StateFlow<UiState<Int>> get() = _userSaved

    fun getArticleById(context: Context, title: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getArticleById(context, title))
        }
    }

    fun getSavedArticleById(title: String) {
        viewModelScope.launch {
            repository.getSavedArticleById(title)
                .catch {
                    _userSaved.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _userSaved.value = UiState.Success(it)
                }
        }
    }

    fun saveArticle(article: ArticleEntity) {
        viewModelScope.launch {
            repository.insertArticle(article)
        }
    }

    fun deleteArticle(article: ArticleEntity) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }
}