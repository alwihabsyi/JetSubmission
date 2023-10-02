package com.alwihbsyi.jetsubmission.ui.screen.bookmark

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alwihbsyi.jetsubmission.R
import com.alwihbsyi.jetsubmission.data.entity.ArticleEntity
import com.alwihbsyi.jetsubmission.di.Injection
import com.alwihbsyi.jetsubmission.ui.ViewModelFactory
import com.alwihbsyi.jetsubmission.ui.common.UiState
import com.alwihbsyi.jetsubmission.ui.components.ArticleItem

@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    viewModel: BookmarkViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    navigateToDetail: (String) -> Unit
) {
    viewModel.uiState.collectAsStateWithLifecycle(UiState.Loading).value.let {
        when (it) {
            is UiState.Loading -> {
                viewModel.getSavedArticles()
            }
            is UiState.Success -> {
                BookmarkContent(
                    article = it.data,
                    navigateToDetail = navigateToDetail,
                    modifier = modifier
                )
            }
            is UiState.Error -> {
                Toast.makeText(LocalContext.current, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun BookmarkContent(
    article: List<ArticleEntity>,
    navigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if(article.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.no_data_saved),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
            )
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
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
