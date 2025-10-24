package com.dimasla4ee.playlistmaker.feature.search.presentation

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.adapter.TrackAdapter
import com.dimasla4ee.playlistmaker.core.presentation.util.setupWindowInsets
import com.dimasla4ee.playlistmaker.core.presentation.util.show
import com.dimasla4ee.playlistmaker.core.util.Keys
import com.dimasla4ee.playlistmaker.databinding.ActivitySearchBinding
import com.dimasla4ee.playlistmaker.feature.player.presentation.PlayerActivity
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.SearchActivityState
import com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var searchResultsAdapter: TrackAdapter
    private val searchHistoryViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater).apply {
            setContentView(root)
            root.setupWindowInsets { insets ->
                val params = clearHistoryButton.layoutParams as? ViewGroup.MarginLayoutParams

                if (params != null) {
                    params.bottomMargin = 10
                    clearHistoryButton.layoutParams = params
                }
            }
            enableEdgeToEdge()
        }

        with(searchHistoryViewModel) {
            uiState.observe(this@SearchActivity) { state ->
                render(state)
            }

            searchQuery.observe(this@SearchActivity) { query ->
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
        searchHistoryViewModel.onTrackClicked(track)

        val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
        intent.putExtra(Keys.TRACK_INFO, track)

        startActivity(intent)
    }

    private fun setupListeners() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        with(binding) {
            clearHistoryButton.setOnClickListener {
                searchHistoryViewModel.onClearSearchHistoryClicked()
            }

            appBar.setNavigationOnClickListener {
                finish()
            }

            queryInput.apply {
                doOnTextChanged { text, _, _, _ ->
                    searchHistoryViewModel.onQueryChanged(text.toString())
                }

                setOnFocusChangeListener { _, hasFocus ->
                    searchHistoryViewModel.onFocusChanged(hasFocus)
                }

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                        searchHistoryViewModel.onSearchClicked()
                        true
                    } else false
                }
            }

            clearQueryButton.setOnClickListener {
                inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                queryInput.apply {
                    searchHistoryViewModel.onClearQueueClicked()
                    clearFocus()
                }
            }

            searchBarLayout.setOnClickListener {
                queryInput.requestFocus()
            }

            retryButton.setOnClickListener {
                searchHistoryViewModel.onRetryClicked()
            }
        }
    }

    private fun render(state: SearchActivityState) {
        with(binding) {
            when (state) {
                is SearchActivityState.Content -> {
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

                is SearchActivityState.Error -> {
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

                is SearchActivityState.Loading -> {
                    loadingIndicator.show(true)
                    stateText.show(false)
                    stateImage.show(false)
                    retryButton.show(false)
                    historyLabel.show(false)
                    clearHistoryButton.show(false)
                    historyRecycler.show(false)
                    resultsRecycler.show(false)
                }

                is SearchActivityState.SearchHistory -> {
                    loadingIndicator.show(false)
                    stateText.show(false)
                    stateImage.show(false)
                    retryButton.show(false)
                    historyLabel.show(true)
                    clearHistoryButton.show(true)
                    historyRecycler.show(true)
                    resultsRecycler.show(false)
                    searchHistoryAdapter.submitList(state.searchHistory)
                }

                is SearchActivityState.NoResults -> {
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
        }
    }
}