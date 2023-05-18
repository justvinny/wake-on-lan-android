package com.example.wake_on_lan_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.wake_on_lan_android.data.MacAddress
import com.example.wake_on_lan_android.data.MacAddressDatabase
import com.example.wake_on_lan_android.data.MacAddressRepository
import com.example.wake_on_lan_android.ui.theme.WakeonlanandroidTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val macAddressRepository by lazy {
        MacAddressRepository(MacAddressDatabase.getDatabase(this).macAddressDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WakeonlanandroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val macAddresses by macAddressRepository.getAll().collectAsState(initial = emptyList())
                    WakeOnLanScreen(
                        macAddresses = macAddresses,
                        saveMacAddress = ::saveMacAddress,
                        deleteMacAddress = ::deleteMacAddress,
                    )
                }
            }
        }
    }

    private fun saveMacAddress(macAddress: String) {
        CoroutineScope(IO).launch {
            macAddressRepository.insert(MacAddress(macAddress))
        }
    }

    private fun deleteMacAddress(macAddress: MacAddress) {
        CoroutineScope(IO).launch {
            macAddressRepository.delete(macAddress)
        }
    }
}
