package com.example.wake_on_lan_android.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MacAddress::class], version = 1, exportSchema = false)
abstract class MacAddressDatabase : RoomDatabase() {
    abstract fun macAddressDao(): MacAddressDao

    companion object {
        private const val DB_NAME = "mac_addresses_db"

        @Volatile
        private var instance: MacAddressDatabase? = null

        fun getDatabase(context: Context): MacAddressDatabase = instance ?: synchronized(this) {
            Room.databaseBuilder(context, MacAddressDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
    }
}
