package com.goldenraven.padawanwallet.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goldenraven.padawanwallet.component.SearchTextField
import java.util.*

@Composable
fun AutoCompleteMnemonic(data:List<String>) {
    val mnemonic = ArrayList<String>()
    if (data.isNotEmpty()) {
        mnemonic.addAll(data)
    }
    val autoCompleteEntities = mnemonic.isAutoCompleteEntities(
        filter = { item, query ->
            item.lowercase(Locale.getDefault()).startsWith(query.lowercase(Locale.getDefault()))
        }
    )
    AutoCompleteBox(
        items = autoCompleteEntities,
        itemContent = { item ->
            MnemonicAutoCompleteItem(item.value)
        }
    ) {
        var value by remember { mutableStateOf("") }
        val view = LocalView.current

        onItemSelected { item ->
            value = item.value
            filter(value)
            view.clearFocus()
        }

        SearchTextField(
            value = value,
            label = "Search Mnemonic",
            onValueChanged = { query ->
                value = query
                filter(value)
            },
            onDoneActionClick = {
                view.clearFocus()
            },
            onClearClick = {
                value = ""
                filter(value)
                view.clearFocus()
            },
            onFocusChanged = { focusState ->
                if (focusState.isFocused) {
                    isSearching = true
                }
            },
            modifier = Modifier.testTag("AutoCompleteSearchBar")
        )
    }
}


@Composable
fun MnemonicAutoCompleteItem(value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = value,
            fontSize = 15.sp,
            color = Color.Black
        )
    }
}