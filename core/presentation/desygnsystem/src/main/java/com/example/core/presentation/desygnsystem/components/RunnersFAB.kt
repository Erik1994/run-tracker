package com.example.core.presentation.desygnsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.presentation.desygnsystem.RunIcon
import com.example.core.presentation.desygnsystem.RunnersTheme

@Composable
fun RunnersFAB(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit,
    contentDescription: String? = null,
    iconSize: Dp = 25.dp
) {
    Box(
        modifier = Modifier
            .size(75.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            .clickable(onClick = onCLick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Preview
@Composable
fun RunnersFABPreview() {
    RunnersTheme {
        RunnersFAB(
            icon = RunIcon,
            onCLick = { }
        )
    }
}