package br.com.plusapps.plusfisio

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.presentation.AuthAppRoot

@Composable
@Preview
fun App() {
    PlusFisioTheme {
        AuthAppRoot()
    }
}
