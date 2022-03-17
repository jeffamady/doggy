package com.amadydev.doggy.ui.breeds

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amadydev.doggy.R
import com.amadydev.doggy.data.models.Dog
import com.amadydev.doggy.databinding.FragmentBreedsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreedsFragment : Fragment(), BreedsAdapter.OnBreedClickListener {
    private var _binding: FragmentBreedsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BreedsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedsBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        setObservers()
        setListeners()
        return binding.root
    }

    private fun setObservers() {
        viewModel.breedsState.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is BreedsViewModel.BreedsState.Success -> {
                        setDataAndShowRecycler(it.dogsList)
                        loading.root.isVisible = false
                        iError.root.isVisible = false
                    }
                    is BreedsViewModel.BreedsState.Loading -> {
                        iError.root.isVisible = false
                        loading.root.isVisible = it.isLoading
                    }
                    is BreedsViewModel.BreedsState.Error -> {
                        rvBreeds.isVisible = false
                        iError.root.isVisible = true
                        iError.tvError.text = getString(it.resId)
                    }
                }
            }
        }
    }

    private fun setDataAndShowRecycler(dogsList: List<Dog>) {
        BreedsAdapter(this).let {
            with(binding.rvBreeds) {
                adapter = it
                isVisible = true
            }
            it.submitList(dogsList)
        }
    }

    private fun setListeners() {
        binding.iError.btnRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.breed_menu, menu)

        val searchItem = menu.findItem(R.id.iSearch)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean = true

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    binding.rvBreeds.scrollToPosition(0)
                    viewModel.searchBreed(query)
                    searchView.clearFocus()
                }
                return true
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBreedClicked(dog: Dog) {
        BreedsFragmentDirections.actionBreedsFragmentToSubBreedsFragment(dog).apply {
            findNavController().navigate(this)
        }
    }
}