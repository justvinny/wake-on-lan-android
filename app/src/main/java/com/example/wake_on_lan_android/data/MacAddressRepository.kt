package com.example.wake_on_lan_android.data

import kotlinx.coroutines.flow.Flow

class MacAddressRepository(private val macAddressDao: MacAddressDao) {

    fun getAll(): Flow<List<MacAddress>> = macAddressDao.getAll()

    suspend fun insert(macAddress: MacAddress) = macAddressDao.insert(macAddress)

    suspend fun delete(macAddress: MacAddress) = macAddressDao.delete(macAddress)
}
