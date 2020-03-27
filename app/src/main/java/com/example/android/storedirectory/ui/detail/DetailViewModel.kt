package com.example.android.storedirectory.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.storedirectory.model.Store

class DetailViewModel : ViewModel() {

    private val _store = MutableLiveData<Store>()
    val store get() = _store

    fun setStoreData(store: Store) {
        _store.value = store
    }
}
