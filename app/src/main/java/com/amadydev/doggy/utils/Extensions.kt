package com.amadydev.doggy.utils

import androidx.recyclerview.widget.DiffUtil
import com.amadydev.doggy.data.models.Dog
import com.amadydev.doggy.ui.subbreeds.SubBreedsAdapter

val dogDiffCallBack = object : DiffUtil.ItemCallback<Dog>() {
    override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean =
        oldItem.breed == newItem.breed

    override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean =
        oldItem == newItem

}

val dataItemDiffCallBack = object : DiffUtil.ItemCallback<SubBreedsAdapter.DataItem>() {
    override fun areItemsTheSame(
        oldItem: SubBreedsAdapter.DataItem,
        newItem: SubBreedsAdapter.DataItem
    ): Boolean =
        oldItem.breed == newItem.breed

    override fun areContentsTheSame(
        oldItem: SubBreedsAdapter.DataItem,
        newItem: SubBreedsAdapter.DataItem
    ): Boolean =
        oldItem == newItem

}