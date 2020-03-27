package com.example.android.storedirectory.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.storedirectory.Resource
import com.example.android.storedirectory.data.ApiErrorResponse
import com.example.android.storedirectory.data.ApiNoNetworkResponse
import com.example.android.storedirectory.data.ApiResponse
import com.example.android.storedirectory.data.ApiSuccessResponse
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): NetworkBoundResource<ResultType, RequestType> {
        withContext(Dispatchers.Main) {
            result.value = Resource.loading(null)
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            val dbResult = loadFromDb()
            if (shouldFetch(dbResult)) {
                fetchFromNetwork(dbResult)
            } else {
                result.value = Resource.success(dbResult)
            }
        }
        return this
    }

    private suspend fun fetchFromNetwork(dbResult: ResultType) {
        setValue(Resource.loading(dbResult))
        when (val apiResponse = createCallAsync().await()) {
            is ApiSuccessResponse -> {
                saveCallResults(processResponse(apiResponse))
                setValue(Resource.success(loadFromDb()))
            }
            is ApiNoNetworkResponse -> {
                setValue(Resource.success(loadFromDb()))
            }
            is ApiErrorResponse -> {
                setValue(Resource.error(apiResponse.errorMessage, loadFromDb()))
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) result.postValue(newValue)
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract suspend fun saveCallResults(items: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType

    @MainThread
    protected abstract suspend fun createCallAsync(): Deferred<ApiResponse<RequestType>>
}