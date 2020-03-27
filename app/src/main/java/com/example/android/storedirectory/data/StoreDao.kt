package com.example.android.storedirectory.data

import androidx.room.*
import com.example.android.storedirectory.model.Store

@Dao
interface StoreDao {
    @Query("SELECT * FROM Store")
    suspend fun getAllStores(): List<Store>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoreListings(listings: List<Store>)

    @Query("DELETE FROM Store")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAndReplaceStoreListings(listings: List<Store>) {
        deleteAll()
        insertStoreListings(listings)
    }
}