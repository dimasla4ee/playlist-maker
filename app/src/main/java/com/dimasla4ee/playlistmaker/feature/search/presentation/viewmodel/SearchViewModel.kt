package com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimasla4ee.playlistmaker.core.domain.consumer.Consumer
import com.dimasla4ee.playlistmaker.core.domain.consumer.ConsumerData
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.util.Debouncer
import com.dimasla4ee.playlistmaker.core.util.LogUtil
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchTracksUseCase
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.SearchScreenState

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<SearchScreenState>(SearchScreenState.Content())
    val uiState: LiveData<SearchScreenState> get() = _uiState

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> get() = _searchQuery
    private val isSearchQueryBlank: Boolean get() = _searchQuery.value.isNullOrBlank()

    private val _searchHistory = MutableLiveData<ArrayDeque<Track>>(ArrayDeque())
    private val searchHistory get() = _searchHistory.value ?: ArrayDeque()
    private val isSearchHistoryEmpty: Boolean get() = _searchHistory.value.isNullOrEmpty()

    private val _results = MutableLiveData<List<Track>>()
    private val results get() = _results.value ?: listOf()

    private val searchRunnable = Runnable { performSearch() }

    init {
        val searchHistory = searchHistoryInteractor.getSearchHistory()
        _searchHistory.postValue(ArrayDeque(searchHistory))
    }

    fun onQueryChanged(newQuery: String) {
        if (_searchQuery.value == newQuery) {
            _uiState.postValue(SearchScreenState.Content(results))
            return
        }

        _searchQuery.value = newQuery

        if (isSearchQueryBlank) {
            Debouncer.cancel(searchRunnable)

            _uiState.postValue(
                if (isSearchHistoryEmpty) {
                    SearchScreenState.Content()
                } else {
                    SearchScreenState.SearchHistory(searchHistory)
                }
            )

            return
        }

        Debouncer.debounce(action = searchRunnable)
    }

    private fun performSearch() {
        val currentQuery = _searchQuery.value ?: return
        if (isSearchQueryBlank) return

        _uiState.postValue(SearchScreenState.Loading)

        searchTracksUseCase.execute(
            query = currentQuery,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    _uiState.postValue(
                        when (data) {
                            is ConsumerData.Data -> {
                                val tracks = data.value
                                LogUtil.d("SearchViewModel", data.value.joinToString())
                                _results.postValue(tracks)
                                if (tracks.isEmpty()) {
                                    SearchScreenState.NoResults
                                } else {
                                    SearchScreenState.Content(tracks)
                                }
                            }

                            is ConsumerData.Error -> SearchScreenState.Error
                        }
                    )
                }
            }
        )
    }

    fun onTrackClicked(track: Track) {
        val tracks = _searchHistory.value ?: return
        val newTracks = ArrayDeque(tracks)

        if (track in newTracks) {
            newTracks.remove(track)
            newTracks.addFirst(track)
        } else if (newTracks.size < MAX_HISTORY_SIZE) {
            newTracks.addFirst(track)
        }

        _searchHistory.postValue(newTracks)

        if (_uiState.value is SearchScreenState.SearchHistory) {
            _uiState.postValue(SearchScreenState.SearchHistory(newTracks))
        }
    }

    fun onClearSearchHistoryClicked() {
        _searchHistory.postValue(ArrayDeque())
        _uiState.postValue(SearchScreenState.Content())
    }

    fun onClearQueueClicked() {
        _results.postValue(listOf())
        _searchQuery.postValue("")
        _uiState.postValue(SearchScreenState.Content())
    }

    fun onSearchClicked() {
        Debouncer.cancel(searchRunnable)
        performSearch()
    }

    fun onRetryClicked() {
        performSearch()
    }

    fun onFocusChanged(hasFocus: Boolean) {
        val newState = if (hasFocus && isSearchQueryBlank && !isSearchHistoryEmpty) {
            SearchScreenState.SearchHistory(searchHistory)
        } else {
            SearchScreenState.Content(results)
        }
        _uiState.postValue(newState)
    }

    override fun onCleared() {
        super.onCleared()
        searchHistoryInteractor.saveHistory(searchHistory)
    }

    companion object {

        private const val MAX_HISTORY_SIZE = 10
    }
}