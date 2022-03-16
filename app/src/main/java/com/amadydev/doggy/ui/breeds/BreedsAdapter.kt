package com.amadydev.doggy.ui.breeds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.amadydev.doggy.R
import com.amadydev.doggy.data.models.Dog
import com.amadydev.doggy.databinding.BreedItemBinding
import com.amadydev.doggy.utils.dogDiffCallBack

class BreedsAdapter(
    private val onBreedClickListener: OnBreedClickListener,
) : ListAdapter<Dog, BreedsAdapter.BreedsViewHolder>(dogDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedsViewHolder =
        BreedsViewHolder(
            BreedItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: BreedsViewHolder, position: Int) =
        holder.bind(position)

    inner class BreedsViewHolder(
        private val binding: BreedItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val dog = getItem(position)
            with(binding) {

                tvBreed.text = dog.breed
                ivBreed.load(dog.imageUrl) {
                    placeholder(R.drawable.dog_logo)
                    crossfade(true)
                    crossfade(500)
                }

                root.setOnClickListener {
                    onBreedClickListener.onBreedClicked(dog)
                }
            }
        }
    }

    interface OnBreedClickListener {
        fun onBreedClicked(dog: Dog)
    }


}