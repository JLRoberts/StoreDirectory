package com.example.android.storedirectory.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Store(
    @PrimaryKey
    val storeID: String,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val zipcode: String,
    val latitude: String,
    val longitude: String,
    val phone: String,
    val storeLogoURL: String
) : Parcelable