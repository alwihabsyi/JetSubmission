package com.alwihbsyi.jetsubmission.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alwihbsyi.jetsubmission.data.entity.ArticleEntity
import com.alwihbsyi.jetsubmission.di.Injection
import com.alwihbsyi.jetsubmission.ui.ViewModelFactory
import com.alwihbsyi.jetsubmission.ui.common.UiState
import com.alwihbsyi.jetsubmission.ui.components.ArticleItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    navigateToDetail: (String) -> Unit
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let {
        when (it) {
            is UiState.Loading -> {
                viewModel.getAllArticles(LocalContext.current)
            }
            is UiState.Success -> {
                HomeContent(
                    article = it.data,
                    navigateToDetail = navigateToDetail
                )
            }
            is UiState.Error -> {
                Toast.makeText(LocalContext.current, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun HomeContent(
    article: List<ArticleEntity>,
    navigateToDetail: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(article, key = { it.title }) {
            ArticleItem(
                image = it.photoUrl ?: "",
                title = it.title,
                description = it.description ?: "",
                modifier = Modifier.clickable {
                    navigateToDetail(it.title)
                }
            )
        }
    }
}