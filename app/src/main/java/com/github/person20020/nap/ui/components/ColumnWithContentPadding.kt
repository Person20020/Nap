package com.github.person20020.nap.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.person20020.nap.constants.ContentPadding

@Composable
fun ColumnWithContentPadding(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ContentPadding,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable (ColumnScope.() -> Unit),
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
                .then(modifier),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        content()
    }
}
