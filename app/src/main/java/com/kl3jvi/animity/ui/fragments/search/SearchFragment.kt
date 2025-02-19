package com.kl3jvi.animity.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kl3jvi.animity.databinding.FragmentSearchBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.collectLatestFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    override val viewModel: SearchViewModel by viewModels()
    private lateinit var pagingController: PagingSearchController

    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pagingController = PagingSearchController(firebaseAnalytics)
        return binding.root
    }

    /**
     * It sets up the search view and recycler view.
     */
    override fun initViews() {
        binding.apply {
            searchRecycler.setController(pagingController)
            mainSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(query: String): Boolean {
                    search(query)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
//                    search(query)
                    return false
                }
            })
        }
    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }


    /**
     * It searches for the query string and updates the viewModel.
     *
     * @param query String - The query string to search for
     */
    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch{ viewModel.queryString.value = query }

                launch {
                    collectLatestFlow(viewModel.searchList) { animeData ->
                        pagingController.submitData(animeData)
                    }
                }
            }
        }
    }

    override fun observeViewModel() {}

    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)
}

