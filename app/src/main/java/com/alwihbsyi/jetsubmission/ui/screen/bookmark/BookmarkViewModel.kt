package com.alwihbsyi.jetsubmission.ui.screen.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alwihbsyi.jetsubmission.data.ArticleRepository
import com.alwihbsyi.jetsubmission.data.entity.ArticleEntity
import com.alwihbsyi.jetsubmission.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val repository: ArticleRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<ArticleEntity>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<ArticleEntity>>> get() = _uiState

    fun getSavedArticles() {
        viewModelScope.launch {
            repository.getSavedArticles()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}