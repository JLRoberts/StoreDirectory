package com.example.android.storedirectory.repository

import androidx.lifecycle.LiveData
import com.example.android.storedirectory.Resource
import com.example.android.storedirectory.data.ApiResponse
import com.example.android.storedirectory.data.ApiService
import com.example.android.storedirectory.data.StoreDao
import com.example.android.storedirectory.model.Store
import com.example.android.storedirectory.model.StoreResponse
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    val apiService: ApiService,
    val storeDao: StoreDao
) {

    suspend fun getStores(): LiveData<Resource<List<Store>>> {
        return object : NetworkBoundResource<List<Store>, StoreResponse>() {
            override fun shouldFetch(data: List<Store>?): Boolean {
                return true
            }

            override suspend fun loadFromDb(): List<Store> {
                return storeDao.getAllStores()
            }

            override suspend fun createCallAsync(): Deferred<ApiResponse<StoreResponse>> {
                return apiService.getStoresAsync()
            }

            override suspend fun saveCallResults(items: StoreResponse) {
                storeDao.deleteAndReplaceStoreListings(items.stores)
            }
        }.build().asLiveData()
    }
}