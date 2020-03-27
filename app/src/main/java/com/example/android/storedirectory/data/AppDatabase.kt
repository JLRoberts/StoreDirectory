package com.example.android.storedirectory.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.storedirectory.model.Store

@Database(entities = [Store::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
}