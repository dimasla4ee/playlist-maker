package com.dimasla4ee.playlistmaker.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.creator.Creator
import com.dimasla4ee.playlistmaker.databinding.ActivitySearchBinding
import com.dimasla4ee.playlistmaker.domain.consumer.Consumer
import com.dimasla4ee.playlistmaker.domain.consumer.ConsumerData
import com.dimasla4ee.playlistmaker.domain.model.Track
import com.dimasla4ee.playlistmaker.domain.use_case.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.presentation.util.setupWindowInsets
import com.dimasla4ee.playlistmaker.presentation.util.show
import com.dimasla4ee.playlistmaker.util.Keys

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchInputEditText: EditText
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var searchResultsAdapter: TrackAdapter
    private lateinit var searchHistory: SearchHistoryInteractor

    private lateinit var handler: Handler

    private var query: String = DEFAULT_QUERY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.root.setupWindowInsets { insets ->
            val params = binding.searchHistoryClearButton.layoutParams
                    as? ViewGroup.MarginLayoutParams

            if (params != null) {
                params.bottomMargin = 10
                binding.searchHistoryClearButton.layoutParams = params
            }
        }

        searchHistory = Creator.provideSearchHistoryInteractor()

        handler = Handler(mainLooper)

        searchInputEditText = binding.searchInputEditText

        searchHistoryAdapter = TrackAdapter { onItemClick(it) }
        searchResultsAdapter = TrackAdapter { onItemClick(it) }

        setContent(ContentType.NONE)
        searchInputEditText.setText(query)

        binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter
        binding.searchResultsRecyclerView.adapter = searchResultsAdapter

        setupListeners()
    }

    private fun onItemClick(track: Track) {
        searchHistory.addTrackToSearchHistory(track)
        searchHistoryAdapter.submitList(searchHistory.getSearchHistory())

        val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
        intent.putExtra(Keys.TRACK_INFO, track)

        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Keys.Preference.SEARCH_QUERY, query)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState.getString(Keys.Preference.SEARCH_QUERY, DEFAULT_QUERY)
    }

    private fun setupListeners() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        binding.searchHistoryClearButton.setOnClickListener {
            searchHistory.clearSearchHistory()
            setContent(ContentType.NONE)
        }

        binding.panelHeader.setOnIconClickListener {
            finish()
        }

        searchInputEditText.apply {
            doOnTextChanged { text, _, _, _ ->
                query = text?.toString() ?: DEFAULT_QUERY
                binding.searchInputClearButton.show(query.isNotEmpty())

                if (query.isEmpty()) {
                    val searchHistoryList = searchHistory.getSearchHistory()
                    searchResultsAdapter.submitList(emptyList())
                    searchHistoryAdapter.submitList(
                        searchHistory.getSearchHistory()
                    )
                    setContent(
                        if (searchHistoryList.isEmpty()) ContentType.NONE else ContentType.SEARCH_HISTORY
                    )
                } else {
                    Debouncer.debounce(action = getSongsRunnable)
                    setContent(ContentType.NONE)
                }
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && query.isEmpty() && searchHistory.getSearchHistory().isNotEmpty()) {
                    setContent(ContentType.SEARCH_HISTORY)
                    searchHistoryAdapter.submitList(
                        searchHistory.getSearchHistory()
                    )
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    fetchSongsAndUpdateUi()
                    true
                } else false
            }
        }

        binding.searchInputClearButton.setOnClickListener {
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            searchInputEditText.apply {
                setText(DEFAULT_QUERY)
                clearFocus()
                setContent(ContentType.NONE)
            }
        }

        binding.searchBarContainerLayout.setOnClickListener {
            searchInputEditText.requestFocus()
        }

        binding.searchStatusReloadButton.setOnClickListener {
            setContent(ContentType.NONE)
            fetchSongsAndUpdateUi()
        }
    }

    override fun onPause() {
        super.onPause()
        searchHistory.saveSearchHistory()
    }

    private fun fetchSongsAndUpdateUi() {
        if (query.isBlank()) {
            setContent(ContentType.SEARCH_HISTORY)
            return
        }

        binding.searchProgressBar.show(true)

        Creator.provideSearchTracksUseCase().execute(
            query = query,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    handler.post {
                        when (data) {
                            is ConsumerData.Data -> {
                                val tracks = data.value
                                searchResultsAdapter.submitList(tracks)
                                setContent(
                                    if (tracks.isEmpty()) ContentType.NO_RESULTS else ContentType.TRACKLIST
                                )
                            }

                            is ConsumerData.Error -> {
                                setContent(ContentType.ERROR)
                            }
                        }
                    }
                }
            }
        )
    }

    private val getSongsRunnable = Runnable { fetchSongsAndUpdateUi() }

    private fun setContent(type: ContentType) {
        binding.apply {
            (type == ContentType.SEARCH_HISTORY).let { isSearchHistory ->
                searchHistoryLabel.show(isSearchHistory)
                searchHistoryClearButton.show(isSearchHistory)
                searchHistoryRecyclerView.show(isSearchHistory)
            }

            searchResultsRecyclerView.show(type == ContentType.TRACKLIST)
            searchProgressBar.show(false)

            if (type == ContentType.NO_RESULTS || type == ContentType.ERROR) {
                searchStatusReloadButton.show(true)
                searchStatusTextView.show(true)
                searchStatusImageView.show(true)
                searchStatusImageView.setImageResource(type.res!!)
                searchStatusTextView.setText(type.text!!)
                searchStatusReloadButton.show(type == ContentType.ERROR)
            } else {
                searchStatusReloadButton.show(false)
                searchStatusTextView.show(false)
                searchStatusImageView.show(false)
            }
        }
    }

    private enum class ContentType(
        @param:DrawableRes val res: Int?,
        @param:StringRes val text: Int?
    ) {
        NONE(null, null),
        SEARCH_HISTORY(null, null),
        TRACKLIST(null, null),
        NO_RESULTS(R.drawable.ic_nothing_found_120, R.string.no_results),
        ERROR(R.drawable.ic_no_internet_120, R.string.network_error)
    }

    private companion object {
        const val DEFAULT_QUERY = ""
    }
}

/*
    1. Move search history and toggle dark theme logic to corresponding repositories.
    (2.) Migrate to kotlinx-serialization.
    (3.) Migrate to DataStore.
 */