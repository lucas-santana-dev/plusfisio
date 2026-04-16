package br.com.plusapps.plusfisio.features.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio

@Composable
fun InitialFlowScaffold(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    contentPadding: PaddingValues = PaddingValues(),
    onBackClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    AuthBackground(contentPadding = contentPadding, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = PlusFisio.spacing.screenHorizontal,
                    vertical = PlusFisio.spacing.screenVertical
                ),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
        ) {
            InitialFlowTopBar(
                title = title,
                onBackClick = onBackClick
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.grid1))
            }
            content()
        }
    }
}
