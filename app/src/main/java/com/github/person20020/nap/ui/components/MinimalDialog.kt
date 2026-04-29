package com.github.person20020.nap.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.person20020.nap.constants.DialogButtonSpacerWidth
import com.github.person20020.nap.constants.DialogCornerRadius

@Composable
fun MinimalDialog(
    onDismissRequest: () -> Unit,
    onConfirmButton: () -> Unit,
    onCancelButton: (() -> Unit)? = null,
    cancelButtonText: String = "Cancel",
    confirmButtonText: String = "OK",
    extraButtonText: String? = null,
    onExtraButton: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(DialogCornerRadius)
        ) {
            Surface() { // TODO: Remove if not needed?
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    content()
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (extraButtonText != null) {
                            TextButton(
                                onClick = { (onExtraButton?:{})() }
                            ) {
                                Text(extraButtonText)
                            }
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                        }
                        TextButton(
                            onClick = {
                                (onCancelButton ?: onDismissRequest)()
                            }
                        ) {
                            Text(cancelButtonText)
                        }
                        Spacer(modifier = Modifier.width(DialogButtonSpacerWidth))
                        TextButton(
                            onClick = onConfirmButton
                        ) {
                            Text(confirmButtonText)
                        }
                    }
                }
            }
        }
    }
}