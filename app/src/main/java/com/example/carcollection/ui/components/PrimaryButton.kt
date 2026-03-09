package com.example.carcollection.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcollection.ui.theme.AppTheme

enum class ButtonSize {
    SMALL, MEDIUM, LARGE
}

data class ButtonProperties(
    val height: Dp,
    val horizontalPadding: Dp,
    val iconSize: Dp,
    val fontSize: TextUnit
)

@Composable
fun PrimaryButton(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.MEDIUM,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    
    val properties = when (size) {
        ButtonSize.SMALL -> ButtonProperties(height = 36.dp, horizontalPadding = 12.dp, iconSize = 14.dp, fontSize = 12.sp)
        ButtonSize.MEDIUM -> ButtonProperties(height = 48.dp, horizontalPadding = 16.dp, iconSize = 16.dp, fontSize = 14.sp)
        ButtonSize.LARGE -> ButtonProperties(height = 56.dp, horizontalPadding = 24.dp, iconSize = 20.dp, fontSize = 16.sp)
    }

    Row(
        modifier = modifier
            .height(properties.height)
            .clip(RoundedCornerShape(if (size == ButtonSize.SMALL) 8.dp else 12.dp))
            .background(if (enabled) colors.primary else colors.primary.copy(alpha = 0.5f))
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = properties.horizontalPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = colors.onPrimary,
            fontWeight = FontWeight.Black,
            fontSize = properties.fontSize,
            letterSpacing = if (size == ButtonSize.SMALL) 0.5.sp else 0.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.onPrimary,
            modifier = Modifier.size(properties.iconSize)
        )
    }
}
