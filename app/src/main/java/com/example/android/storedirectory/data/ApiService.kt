package com.example.android.storedirectory.data

import com.example.android.storedirectory.model.StoreResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ApiService {
    @GET("/BR_Android_CodingExam_2015_Server/stores.json")
    fun getStoresAsync(): Deferred<ApiResponse<StoreResponse>>
}