package com.alwihbsyi.jetsubmission.ui.screen.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.alwihbsyi.jetsubmission.di.Injection
import com.alwihbsyi.jetsubmission.ui.ViewModelFactory
import com.alwihbsyi.jetsubmission.ui.common.UiState
import com.alwihbsyi.jetsubmission.ui.components.ActionButton
import com.alwihbsyi.jetsubmission.ui.theme.JetSubmissionTheme

@Composable
fun DetailScreen(
    title: String,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(LocalContext.current)
        )
    ),
    onButtonClick: (url: String) -> Unit,
    navigateBack: () -> Unit
) {
    var isArticleSaved by remember { mutableStateOf(false) }

    viewModel.userSaved.collectAsState(UiState.Loading).value.let {
        when (it) {
            is UiState.Loading -> {
                viewModel.getSavedArticleById(title)
            }

            is UiState.Success -> {
                isArticleSaved = it.data == 1
            }

            else -> {
                isArticleSaved = false
            }
        }
    }

    viewModel.uiState.collectAsState(UiState.Loading).value.let {
        when (it) {
            is UiState.Loading -> {
                viewModel.getArticleById(LocalContext.current, title)
            }

            is UiState.Success -> {
                val data = it.data
                DetailContent(
                    image = data.photoUrl ?: "",
                    title = data.title,
                    content = data.content ?: "",
                    url = data.url ?: "",
                    onBackClick = navigateBack,
                    onFavoriteClick = {
                        if (isArticleSaved) {
                            viewModel.deleteArticle(data)
                        } else {
                            viewModel.saveArticle(data)
                        }
                    },
                    isArticleSaved = isArticleSaved,
                    onButtonClick = onButtonClick
                )
            }

            is UiState.Error -> {
                Toast.makeText(LocalContext.current, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun DetailContent(
    image: String,
    title: String,
    content: String,
    url: String,
    isArticleSaved: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onButtonClick: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val iconColor = if (isArticleSaved) remember { mutableStateOf(Color.Red) } else remember {
        mutableStateOf(Color.Unspecified)
    }
    val iconVector = if (isArticleSaved) remember { mutableStateOf(Icons.Default.Favorite) } else remember {
        mutableStateOf(Icons.Default.FavoriteBorder)
    }

    Column(modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(model = image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .height(400.dp)
                        .fillMaxWidth()
                )
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackClick() }
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            onFavoriteClick()
                            if (isArticleSaved) {
                                iconColor.value = Color.Unspecified
                                iconVector.value = Icons.Default.FavoriteBorder
                            } else {
                                iconColor.value = Color.Red
                                iconVector.value = Icons.Default.Favorite
                            }
                        }
                        .clip(RoundedCornerShape(10.dp))
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = iconVector.value,
                        tint = iconColor.value,
                        contentDescription = "Bookmark",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(Color.LightGray)
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ActionButton {
                onButtonClick(url)
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DetailContentPreview() {
    JetSubmissionTheme {
        DetailContent(
            image = "https://akcdn.detik.net.id/api/wm/2023/01/04/latihan-keras-timnas-indonesia-jelang-laga-lawan-vietnam-4_169.jpeg?wid=54&w=650&v=1&t=jpeg",
            title = "JUDUL BERITA NIH",
            content = "ASDSADASDSADSADs",
            url = "asdasdasdasdasd",
            onBackClick = {},
            onFavoriteClick = {},
            onButtonClick = {},
            isArticleSaved = false
        )
    }
}