package com.example.wake_on_lan_android.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MacAddressDao {
    @Query("SELECT * from $MAC_ADDRESSES_TABLE")
    fun getAll(): Flow<List<MacAddress>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: MacAddress)

    @Delete
    suspend fun delete(item: MacAddress)
}
