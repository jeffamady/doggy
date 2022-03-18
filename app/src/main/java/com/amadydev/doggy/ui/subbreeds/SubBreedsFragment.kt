package com.amadydev.doggy.ui.subbreeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.amadydev.doggy.R
import com.amadydev.doggy.data.models.Dog
import com.amadydev.doggy.databinding.FragmentSubBreedsBinding
import com.amadydev.doggy.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubBreedsFragment : Fragment() {
    private var _binding: FragmentSubBreedsBinding? = null
    private val binding get() = _binding!!
    private val args: SubBreedsFragmentArgs by navArgs()
    private val viewModel: SubBreedsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubBreedsBinding.inflate(inflater, container, false)

        setupUi()
        setObservers()
        setListeners()
        return binding.root
    }

    private fun setObservers() {
        viewModel.subBreedsState.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is SubBreedsViewModel.SubBreedsState.Success -> {
                        setDataAndShowRecycler(it.dogsList)
                        subBreedsContainer.isVisible = true
                        iError.root.isVisible = false
                        loading.root.isVisible = false
                    }
                    is SubBreedsViewModel.SubBreedsState.Loading -> {
                        subBreedsContainer.isVisible = false
                        iError.root.isVisible = false
                        loading.root.isVisible = it.isLoading
                        swipeRefreshLayout.isRefreshing = false
                    }
                    is SubBreedsViewModel.SubBreedsState.Error -> {
                        subBreedsContainer.isVisible = false
                        iError.root.isVisible = true
                        iError.tvError.text = getString(it.resId)
                    }
                    SubBreedsViewModel.SubBreedsState.Empty -> {
                        subBreedsContainer.isVisible = true
                        iEmpty.root.isVisible = true
                        iEmpty.tvNoSubBreeds.text =
                            getString(R.string.no_sub_breeds, args.dog.breed)
                        iError.root.isVisible = false
                    }
                }
            }
        }
    }

    private fun setupUi() {
        val dog = args.dog
        (activity as MainActivity).apply {
            setActionBarTitleAndAllowBack(dog.breed, true)
        }
        with(binding) {
            ivBreed.load(dog.imageUrl)
        }
        // get all sub breeds
        viewModel.getAllSubBreeds(dog.breed)
    }

    private fun setListeners() {
        with(binding) {
            // Retry
            iError.btnRetry.setOnClickListener {
                setupUi()
            }

            swipeRefreshLayout.setOnRefreshListener {
                setupUi()
            }
        }
    }

    private fun setDataAndShowRecycler(dogsList: List<Dog>) {
        SubBreedsAdapter().let {
            with(binding.rvSubBreeds) {
                adapter = it
                isVisible = true
            }
            it.addHeaderAndSubmitList(dogsList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).apply {
            setActionBarTitleAndAllowBack()
        }
        _binding = null
    }

}