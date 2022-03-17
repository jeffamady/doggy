package com.amadydev.doggy.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dog(
    val breed: String,
    var imageUrl: String = ""
) : Parcelable
