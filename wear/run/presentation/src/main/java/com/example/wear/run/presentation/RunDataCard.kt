package com.example.wear.run.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.core.presentation.desygnsystem.dimentions.LocalDimensions

@Composable
fun RunDataCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    valueTextColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimensions.dimenSmall)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp
        )
        Text(
            text = value,
            color = valueTextColor,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}