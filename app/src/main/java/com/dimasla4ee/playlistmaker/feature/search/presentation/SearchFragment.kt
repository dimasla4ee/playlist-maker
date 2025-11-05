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
import com.dimasla4ee.playlistmaker.core.presentation.util.setTopDrawable
import com.dimasla4ee.playlistmaker.core.presentation.util.show
import com.dimasla4ee.playlistmaker.core.presentation.util.viewBinding
import com.dimasla4ee.playlistmaker.core.util.LogUtil
import com.dimasla4ee.playlistmaker.databinding.FragmentSearchBinding
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.SearchUiState
import com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel.SearchViewModel
import com.google.android.material.search.SearchView
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private lateinit var recyclerAdapter: TrackAdapter
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(searchViewModel) {
            uiState.observe(viewLifecycleOwner) { state ->
                render(state)
            }
        }

        recyclerAdapter = TrackAdapter { onItemClick(it) }
        binding.recycler.adapter = recyclerAdapter

        setupListeners()
    }

    override fun onPause() {
        super.onPause()
        searchViewModel.onPause()
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
        ) as InputMethodManager

        val clearButtonSearchBar = binding.searchBar.menu.findItem(
            R.id.actionClear
        ).apply { isVisible = false }

        with(binding) {
            clearHistoryButton.setOnClickListener {
                searchViewModel.onClearSearchHistoryClicked()
            }

            searchView.editText.doOnTextChanged { charSequence, _, _, _ ->
                val text = charSequence.toString()
                searchViewModel.onQueryChanged(text)
                clearButtonSearchBar?.isVisible = text.isNotEmpty()
            }

            searchView.editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchViewModel.onSearchClicked()
                    inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
                    true
                } else false
            }

            searchView.addTransitionListener { _, _, newState ->
                if (newState == SearchView.TransitionState.HIDING) {
                    searchBar.setText(searchView.editText.text)
                }
            }

            searchBar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.actionClear) {
                    searchViewModel.onClearQueueClicked()
                    searchBar.clearText()
                    true
                } else {
                    false
                }
            }

            retryButton.setOnClickListener {
                searchViewModel.onRetryClicked()
            }
        }
    }

    private fun render(state: SearchUiState) {
        LogUtil.d("SearchFragment", "render() called with: state = $state")
        when (state) {
            is SearchUiState.Content -> showContent(state)
            is SearchUiState.Error -> showError()
            is SearchUiState.Loading -> showLoading()
            is SearchUiState.NoResults -> showNoResults()
            is SearchUiState.History -> showHistory(state)
            is SearchUiState.Idle -> showIdle()
        }
    }

    private fun updateUiVisibility(
        recyclerAdapterList: List<Track>,
        loadingIndicatorVisible: Boolean,
        retryButtonVisible: Boolean,
        historyLabelVisible: Boolean,
        clearHistoryButtonVisible: Boolean,
        stateContainerVisible: Boolean,
        stateInfoText: String?,
        stateInfoDrawable: Int?
    ) {
        recyclerAdapter.submitList(recyclerAdapterList)
        with(binding) {
            loadingIndicator.show(loadingIndicatorVisible)
            retryButton.show(retryButtonVisible)
            historyLabel.show(historyLabelVisible)
            clearHistoryButton.show(clearHistoryButtonVisible)
            stateContainer.show(stateContainerVisible)

            if (stateContainerVisible) {
                stateInfo.apply {
                    text = stateInfoText
                    setTopDrawable(stateInfoDrawable ?: 0)
                }
            }
        }
    }

    private fun showContent(state: SearchUiState.Content) = updateUiVisibility(
        recyclerAdapterList = state.results,
        loadingIndicatorVisible = false,
        retryButtonVisible = false,
        historyLabelVisible = false,
        clearHistoryButtonVisible = false,
        stateContainerVisible = false,
        stateInfoText = null,
        stateInfoDrawable = null
    )

    private fun showError() = updateUiVisibility(
        recyclerAdapterList = emptyList(),
        loadingIndicatorVisible = false,
        retryButtonVisible = true,
        historyLabelVisible = false,
        clearHistoryButtonVisible = false,
        stateContainerVisible = true,
        stateInfoText = getString(R.string.network_error),
        stateInfoDrawable = R.drawable.ic_no_internet_120
    )

    private fun showNoResults() = updateUiVisibility(
        recyclerAdapterList = emptyList(),
        loadingIndicatorVisible = false,
        retryButtonVisible = false,
        historyLabelVisible = false,
        clearHistoryButtonVisible = false,
        stateContainerVisible = true,
        stateInfoText = getString(R.string.no_results),
        stateInfoDrawable = R.drawable.ic_nothing_found_120
    )

    private fun showLoading() = updateUiVisibility(
        recyclerAdapterList = emptyList(),
        loadingIndicatorVisible = true,
        retryButtonVisible = false,
        historyLabelVisible = false,
        clearHistoryButtonVisible = false,
        stateContainerVisible = false,
        stateInfoText = null,
        stateInfoDrawable = null
    )

    private fun showHistory(state: SearchUiState.History) = updateUiVisibility(
        recyclerAdapterList = state.history,
        loadingIndicatorVisible = false,
        retryButtonVisible = false,
        historyLabelVisible = true,
        clearHistoryButtonVisible = true,
        stateContainerVisible = false,
        stateInfoText = null,
        stateInfoDrawable = null
    )

    private fun showIdle() = updateUiVisibility(
        recyclerAdapterList = emptyList(),
        loadingIndicatorVisible = false,
        retryButtonVisible = false,
        historyLabelVisible = false,
        clearHistoryButtonVisible = false,
        stateContainerVisible = false,
        stateInfoText = null,
        stateInfoDrawable = null
    )
}
