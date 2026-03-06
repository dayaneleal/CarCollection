package com.example.carcollection.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    val colors = AppTheme.colors
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .then(if (showBackground) Modifier.background(colors.surface) else Modifier)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = detail.icon,
            contentDescription = null,
            tint = colors.onSurface.copy(alpha = 0.4f),
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = detail.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
        detail.description?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                fontSize = 12.sp,
                color = colors.onSurface.copy(alpha = 0.4f),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}
