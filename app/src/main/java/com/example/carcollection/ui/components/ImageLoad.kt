package com.example.carcollection.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.example.carcollection.ui.theme.AppTheme
import java.net.UnknownHostException

@Composable
fun ImageLoad(url: String, modifier: Modifier = Modifier) {
    var currentPainterState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
    val colors = AppTheme.colors

    SubcomposeAsyncImage(
        model = url,
        contentDescription = "Imagem do carro",
        modifier = modifier,
        contentScale = ContentScale.Crop,
        onState = { currentPainterState = it }
    ) {
        when (val state = currentPainterState) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.surface.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = colors.primary.copy(alpha = 0.5f)
                    )
                }
            }

            is AsyncImagePainter.State.Error -> {
                val errorDetail = when (state.result.throwable) {
                    is UnknownHostException -> ErrorDetail(
                        title = "Sem conexão",
                        icon = Icons.Default.CloudOff,
                        description = "Verifique sua internet"
                    )
                    else -> ErrorDetail(
                        title = "Erro de imagem",
                        icon = Icons.Default.BrokenImage,
                        description = "URL inválida ou corrompida"
                    )
                }

                ErrorDisplay(
                    detail = errorDetail,
                    iconSize = 24.dp,
                    showBackground = true
                )
                
                Log.e("ImageLoad", "Falha na URL: $url | Motivo: ${state.result.throwable.message}")
            }
            
            else -> {
                SubcomposeAsyncImageContent()
            }
        }
    }
}
