package br.com.plusapps.plusfisio.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme

@Composable
fun PlusFisioTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    trailingLabel: String? = null,
    onTrailingClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        supportingText = supportingText?.let { { Text(it) } },
        singleLine = singleLine,
        isError = isError,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        shape = PlusFisio.shapes.control,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = PlusFisio.colors.line,
            errorBorderColor = PlusFisio.colors.error,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        trailingIcon = trailingLabel?.let {
            {
                Text(
                    text = trailingLabel,
                    modifier = Modifier
                        .clip(PlusFisio.shapes.pill)
                        .clickable(
                            enabled = onTrailingClick != null,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTrailingClick?.invoke() }
                        .padding(horizontal = PlusFisio.spacing.grid2, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
fun PlusFisioSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    supportingText: String? = null
) {
    PlusFisioTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        supportingText = supportingText,
        singleLine = true
    )
}

@Composable
fun PlusFisioSelectField(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    isError: Boolean = false
) {
    PlusFisioTextField(
        value = value,
        onValueChange = {},
        label = label,
        modifier = modifier.clickable(onClick = onClick),
        supportingText = supportingText,
        isError = isError,
        singleLine = true,
        readOnly = true,
        trailingLabel = "v",
        onTrailingClick = onClick
    )
}

@Composable
fun PlusFisioDateField(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    isError: Boolean = false
) {
    PlusFisioSelectField(
        value = value,
        label = label,
        onClick = onClick,
        modifier = modifier,
        supportingText = supportingText,
        isError = isError
    )
}

@Preview
@Composable
private fun PlusFisioFieldsPreview() {
    PlusFisioTheme {
        Column(
            modifier = Modifier.padding(PlusFisio.spacing.grid4)
        ) {
            PlusFisioTextField(
                value = "",
                onValueChange = {},
                label = "Nome completo",
                placeholder = "Digite o nome completo",
                singleLine = true
            )
            PlusFisioTextField(
                value = "Juliana Martins",
                onValueChange = {},
                label = "Nome completo",
                supportingText = "Preencha este campo corretamente",
                isError = true,
                singleLine = true
            )
            PlusFisioSelectField(
                value = "Ana Souza",
                label = "Profissional",
                onClick = {}
            )
        }
    }
}
