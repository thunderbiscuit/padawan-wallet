package com.goldenraven.padawanwallet.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.goldenraven.padawanwallet.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun copyToClipboard(content: String, context: Context, scope: CoroutineScope, snackbarHostState: SnackbarHostState, setCopyClicked: ((Boolean) -> Unit)?) {
    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("", content)
    clipboard.setPrimaryClip(clip)
    scope.launch {
        snackbarHostState.showSnackbar("Copied address to clipboard!")
        delay(1000)
        if (setCopyClicked != null) {
            setCopyClicked(false)
        }
    }
}
