package com.amadydev.doggy.ui.breeds

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.amadydev.doggy.R
import com.amadydev.doggy.data.models.Dog
import com.amadydev.doggy.databinding.FragmentBreedsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreedsFragment : Fragment(), BreedsAdapter.OnBreedClickListener {
    private var _binding: FragmentBreedsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BreedsViewModel by viewModels()
    private lateinit var breedsAdapter: BreedsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedsBinding.inflate(inflater, container, false)

        breedsAdapter = BreedsAdapter(this)
        binding.rvBreeds.adapter = breedsAdapter

        setHasOptionsMenu(true)
        setObservers()
        setPagination()
        setListeners()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers() {
        viewModel.breedsState.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is BreedsViewModel.BreedsState.Success -> {
                        with(breedsAdapter) {
                            submitList(it.dogsList)
                            notifyDataSetChanged()
                        }
                        loading.root.isVisible = false
                        iError.root.isVisible = false
                        rvBreeds.isVisible = true
                    }
                    is BreedsViewModel.BreedsState.Loading -> {
                        iError.root.isVisible = false
                        loading.root.isVisible = it.isLoading
                        swipeRefreshLayout.isRefreshing = false
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


    private fun setListeners() {
        with(binding) {
            iError.btnRetry.setOnClickListener {
                viewModel.retry()
            }

            // Refresh the screen
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.retry()
            }
        }
    }

    private fun setPagination() {
        binding.rvBreeds.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadImages()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
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
                    viewModel.searchBreed(query.lowercase())
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