package com.amadydev.doggy.ui.subbreeds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.amadydev.doggy.R
import com.amadydev.doggy.data.models.Dog
import com.amadydev.doggy.databinding.BreedItemBinding
import com.amadydev.doggy.utils.Constants
import com.amadydev.doggy.utils.dataItemDiffCallBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubBreedsAdapter :
    ListAdapter<SubBreedsAdapter.DataItem, RecyclerView.ViewHolder>(
        dataItemDiffCallBack
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.ITEM_VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_layout, parent, false)
                HeaderViewHolder(view)
            }
            Constants.ITEM_VIEW_TYPE_DOG -> {
                SubBreedsViewHolder(
                    BreedItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SubBreedsViewHolder -> {
                holder.bind(position)
            }
        }
    }

    //    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
//        CoroutineScope(Dispatchers.Default).launch {
//            val items: List<DataItem> = when (list) {
//                null -> listOf(DataItem.Header)
//                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
//            }
//            withContext(Dispatchers.Main){
//                submitList(items)
//            }
//        }
//    }
    fun addHeaderAndSubmitList(list: List<Dog>) {
        CoroutineScope(Dispatchers.Default).launch {
            val items: List<DataItem> =
                listOf(DataItem.Header) + list.map { DataItem.DogItem(it) }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> Constants.ITEM_VIEW_TYPE_HEADER
            is DataItem.DogItem -> Constants.ITEM_VIEW_TYPE_DOG
        }
    }

    inner class SubBreedsViewHolder(
        private val binding: BreedItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val dog = (getItem(position) as DataItem.DogItem).dog
            with(binding) {
                tvBreed.text = dog.breed
                ivBreed.load(dog.imageUrl) {
                    placeholder(R.drawable.dog_logo)
                    crossfade(true)
                    crossfade(500)
                }
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)


    sealed class DataItem {
        abstract val breed: String

        data class DogItem(val dog: Dog) : DataItem() {
            override val breed: String = dog.breed
        }

        object Header : DataItem() {
            override val breed: String = ""
        }
    }

}