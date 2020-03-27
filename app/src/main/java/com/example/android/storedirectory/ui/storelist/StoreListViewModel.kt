package com.example.android.storedirectory.ui.storelist

import androidx.lifecycle.*
import com.example.android.storedirectory.Resource
import com.example.android.storedirectory.Status
import com.example.android.storedirectory.model.Store
import com.example.android.storedirectory.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoreListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var storesSource: LiveData<Resource<List<Store>>> = MutableLiveData()
    private val _stores = MediatorLiveData<Resource<List<Store>>>()
    val stores: LiveData<Resource<List<Store>>> get() = _stores

    init {
        getStoreData()
    }

    fun getStoreData() = viewModelScope.launch(Dispatchers.Main) {
        _stores.removeSource(storesSource)
        withContext(Dispatchers.IO) {
            storesSource = repository.getStores()
        }
        _stores.addSource(storesSource) {
            _stores.value = it
        }
    }
}
