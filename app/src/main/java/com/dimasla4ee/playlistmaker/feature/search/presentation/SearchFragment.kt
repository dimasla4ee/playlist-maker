package com.dimasla4ee.playlistmaker.feature.search.presentation

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.adapter.TrackAdapter
import com.dimasla4ee.playlistmaker.core.presentation.util.show
import com.dimasla4ee.playlistmaker.core.presentation.util.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentSearchBinding
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.SearchScreenState
import com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var searchResultsAdapter: TrackAdapter
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(searchViewModel) {
            uiState.observe(viewLifecycleOwner) { state ->
                render(state)
            }

            searchQuery.observe(viewLifecycleOwner) { query ->
                with(binding) {
                    if (queryInput.text.toString() != query) {
                        queryInput.setText(query)
                    }
                    clearQueryButton.show(query.isNotBlank())
                }
            }
        }

        searchHistoryAdapter = TrackAdapter { onItemClick(it) }
        searchResultsAdapter = TrackAdapter { onItemClick(it) }

        binding.historyRecycler.adapter = searchHistoryAdapter
        binding.resultsRecycler.adapter = searchResultsAdapter

        setupListeners()
    }

    private fun onItemClick(track: Track) {
        searchViewModel.onTrackClicked(track)

        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
        )
    }

    private fun setupListeners() {
        val inputMethodManager = requireContext().getSystemService(
            INPUT_METHOD_SERVICE
        ) as? InputMethodManager

        with(binding) {
            clearHistoryButton.setOnClickListener {
                searchViewModel.onClearSearchHistoryClicked()
            }

            queryInput.apply {
                doOnTextChanged { text, _, _, _ ->
                    searchViewModel.onQueryChanged(text.toString())
                }

                setOnFocusChangeListener { _, hasFocus ->
                    searchViewModel.onFocusChanged(hasFocus)
                }

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
                        searchViewModel.onSearchClicked()
                        true
                    } else false
                }
            }

            clearQueryButton.setOnClickListener {
                queryInput.apply {
                    searchViewModel.onClearQueueClicked()
                    clearFocus()
                }
            }

            searchBarLayout.setOnClickListener {
                queryInput.requestFocus()
            }

            retryButton.setOnClickListener {
                searchViewModel.onRetryClicked()
            }
        }
    }

    private fun render(state: SearchScreenState) {
        with(binding) {
            when (state) {
                is SearchScreenState.Content -> {
                    showContent(state)
                }

                is SearchScreenState.Error -> {
                    showError()
                }

                is SearchScreenState.Loading -> {
                    showLoading()
                }

                is SearchScreenState.SearchHistory -> {
                    showHistory(state)
                }

                is SearchScreenState.NoResults -> {
                    showNoResults()
                }
            }
        }
    }

    private fun showContent(state: SearchScreenState.Content) {
        with(binding) {
            loadingIndicator.show(false)
            stateText.show(false)
            stateImage.show(false)
            retryButton.show(false)
            historyLabel.show(false)
            clearHistoryButton.show(false)
            historyRecycler.show(false)
            resultsRecycler.show(true)
            searchResultsAdapter.submitList(state.results)
        }
    }

    private fun showError() {
        with(binding) {
            loadingIndicator.show(false)
            retryButton.show(true)
            historyLabel.show(false)
            clearHistoryButton.show(false)
            historyRecycler.show(false)
            resultsRecycler.show(false)
            stateText.apply {
                show(true)
                setText(R.string.network_error)
            }
            stateImage.apply {
                show(true)
                setImageResource(R.drawable.ic_no_internet_120)
            }
        }
    }

    private fun showHistory(state: SearchScreenState.SearchHistory) {
        with(binding) {
            historyRecycler.show(true)
            historyLabel.show(true)
            clearHistoryButton.show(true)
            searchHistoryAdapter.submitList(state.searchHistory)

            loadingIndicator.show(false)
            stateText.show(false)
            stateImage.show(false)
            retryButton.show(false)
            resultsRecycler.show(false)
        }
    }

    private fun showNoResults() {
        with(binding) {
            loadingIndicator.show(false)
            retryButton.show(false)
            historyLabel.show(false)
            clearHistoryButton.show(false)
            historyRecycler.show(false)
            resultsRecycler.show(false)
            stateText.apply {
                show(true)
                setText(R.string.no_results)
            }
            stateImage.apply {
                show(true)
                setImageResource(R.drawable.ic_nothing_found_120)
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            loadingIndicator.show(true)
            stateText.show(false)
            stateImage.show(false)
            retryButton.show(false)
            historyLabel.show(false)
            clearHistoryButton.show(false)
            historyRecycler.show(false)
            resultsRecycler.show(false)
        }
    }
}