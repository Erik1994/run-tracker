@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.core.presentation.desygnsystem.components.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.desygnsystem.AnalyticsIcon
import com.example.core.presentation.desygnsystem.ArrowLeftIcon
import com.example.core.presentation.desygnsystem.LogoIcon
import com.example.core.presentation.desygnsystem.Poppins
import com.example.core.presentation.desygnsystem.R
import com.example.core.presentation.desygnsystem.RunnersGreen
import com.example.core.presentation.desygnsystem.RunnersTheme
import com.example.core.presentation.desygnsystem.dimentions.LocalDimensions


@Composable
fun RunnersToolbar(
    modifier: Modifier = Modifier,
    showBackButton: Boolean,
    title: String,
    menuItems: List<DropDownItem> = emptyList(),
    onMenuItemClick: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null
) {
    var isDropDownOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val dimensions = LocalDimensions.current
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                startContent?.invoke()
                Spacer(modifier = Modifier.width(dimensions.dimenSmall))
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = Poppins
                )
            }
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = ArrowLeftIcon,
                        contentDescription = stringResource(id = R.string.go_back)
                    )
                }
            }
        },
        actions = {
            if (menuItems.isNotEmpty()) {
                Box(modifier = Modifier) {
                    DropdownMenu(
                        expanded = isDropDownOpen,
                        onDismissRequest = { isDropDownOpen = false }
                    ) {
                        menuItems.forEachIndexed { index, dropDownItem ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { onMenuItemClick(index) }
                                    .fillMaxWidth()
                                    .padding(dimensions.dimenMedium)
                            ) {
                                Icon(
                                    imageVector = dropDownItem.icon,
                                    contentDescription = dropDownItem.title
                                )
                                Spacer(modifier = Modifier.width(dimensions.dimenSmall))
                                Text(text = dropDownItem.title)
                            }
                        }
                    }
                    IconButton(onClick = { isDropDownOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(
                                id = R.string.open_menu
                            ),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )
}


@Preview
@Composable
fun RunnersToolbarPreview() {
    RunnersTheme {
        RunnersToolbar(
            showBackButton = false,
            title = "Runners ",
            modifier = Modifier.fillMaxWidth(),
            startContent = {
                Icon(
                    imageVector = LogoIcon,
                    contentDescription = null,
                    tint = RunnersGreen,
                    modifier = Modifier.size(35.dp)
                )
            },
            menuItems = listOf(
                DropDownItem(
                    icon = AnalyticsIcon,
                    "Analytics"
                )
            )
        )
    }
}