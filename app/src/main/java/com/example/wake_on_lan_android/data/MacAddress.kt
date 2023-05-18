package com.example.wake_on_lan_android.data

import androidx.room.Entity
import androidx.room.PrimaryKey

const val MAC_ADDRESSES_TABLE = "mac_addresses"

@Entity(tableName  = MAC_ADDRESSES_TABLE)
data class MacAddress(@PrimaryKey val macAddress: String)
