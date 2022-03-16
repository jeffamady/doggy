package com.amadydev.doggy.utils

import androidx.recyclerview.widget.DiffUtil
import com.amadydev.doggy.data.models.Dog

fun List<String>.toDogList() = this.map { Dog(it) }

val dogDiffCallBack = object : DiffUtil.ItemCallback<Dog>() {
    override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean =
        oldItem.breed == newItem.breed

    override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean =
        oldItem == newItem

}