package com.example.wake_on_lan_android

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wake_on_lan_android.data.MacAddress
import com.example.wake_on_lan_android.ui.theme.WakeonlanandroidTheme
import kotlinx.coroutines.launch

@Composable
fun WakeOnLanScreen(
    macAddresses: List<MacAddress>,
    saveMacAddress: (String) -> Unit,
    deleteMacAddress: (MacAddress) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        val sharedModifier = Modifier.fillMaxWidth()
        SaveMacAddress(modifier = sharedModifier, saveMacAddress = saveMacAddress)
        MacAddressList(
            modifier = sharedModifier,
            macAddresses = macAddresses,
            deleteMacAddress = deleteMacAddress,
        )
    }
}

@Composable
private fun SaveMacAddress(
    modifier: Modifier,
    saveMacAddress: (String) -> Unit,
) {
    var macAddress by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(true) }
    OutlinedTextField(
        modifier = modifier,
        value = macAddress,
        onValueChange = {
            macAddress = it
            isError = !isValidMacAddress(it)
        },
        isError = isError,
        label = { Text(stringResource(id = R.string.text_field_mac_address)) },
    )

    if (isError) {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.error_msg_invalid_mac_address),
            style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.error),
        )
    } else {
        Spacer(modifier = Modifier.height(16.dp))
    }

    Button(
        modifier = modifier.then(Modifier.padding(top = 4.dp, bottom = 8.dp)),
        onClick = { saveMacAddress(macAddress) },
        enabled = !isError,
    ) {
        Text(stringResource(id = R.string.btn_save))
    }
}

@Composable
private fun ColumnScope.MacAddressList(
    modifier: Modifier,
    macAddresses: List<MacAddress>,
    deleteMacAddress: (MacAddress) -> Unit,
) {
    if (macAddresses.isEmpty()) return

    val scope = rememberCoroutineScope()

    Text(
        text = stringResource(id = R.string.title_saved_mac_addresses),
        style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.primary),
    )

    LazyColumn(
        modifier = modifier.then(
            Modifier
                .weight(1f)
                .padding(top = 8.dp)
        ),
    ) {
        items(macAddresses) { item ->
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(item.macAddress)

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {
                    deleteMacAddress(item)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.content_delete_mac_address),
                        tint = MaterialTheme.colors.error,
                    )
                }

                IconButton(onClick = {
                    scope.launch {
                        sendMagicPacket(item.macAddress)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Computer,
                        contentDescription = stringResource(id = R.string.content_wake_on_lan),
                        tint = MaterialTheme.colors.primary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewWakeOnLanScreen() = WakeonlanandroidTheme {
    WakeOnLanScreen(
        macAddresses = listOf(
            MacAddress("00:11:22:33:44:53"),
            MacAddress("00:11:22:33:44:54"),
            MacAddress("00:11:22:33:44:55"),
        ),
        saveMacAddress = {},
        deleteMacAddress = {},
    )
}
