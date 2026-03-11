package com.example.carcollection.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcollection.ui.theme.AppTheme

data class ErrorDetail(
    val title: String,
    val icon: ImageVector,
    val description: String? = null
)

@Composable
fun ErrorDisplay(
    detail: ErrorDetail,
    modifier: Modifier = Modifier,
    iconSize: Dp = 28.dp,
    showBackground: Boolean = true
) {
    if (showBackground) {
        Card(
            modifier = modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface)
        ) {
            ErrorContent(Modifier.fillMaxSize(),detail, iconSize, )
        }
    } else {
        ErrorContent(Modifier.fillMaxSize(), detail, iconSize,)
    }
}

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    detail: ErrorDetail,
    iconSize: Dp = 28.dp,
) {
    val colors = AppTheme.colors
    
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = detail.icon,
            contentDescription = null,
            tint = colors.onSurface.copy(alpha = 0.4f),
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = detail.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        detail.description?.let {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = it,
                fontSize = 14.sp,
                color = colors.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}
