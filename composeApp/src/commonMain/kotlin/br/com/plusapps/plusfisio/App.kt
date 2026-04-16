package br.com.plusapps.plusfisio

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.plusapps.plusfisio.di.initKoin
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.root.presentation.AppRoot

@Composable
@Preview
fun App() {
    initKoin()

    PlusFisioTheme {
        AppRoot()
    }
}
