package com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.util.LogUtil
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchTracksUseCase
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.SearchUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<SearchUiState>(SearchUiState.Idle)
    val uiState: LiveData<SearchUiState> get() = _uiState

    private val _searchQuery = MutableLiveData("")
    private val searchQuery: String get() = _searchQuery.value ?: ""

    private val _searchHistory = MutableLiveData<ArrayDeque<Track>>(ArrayDeque())
    private val searchHistory get() = _searchHistory.value ?: ArrayDeque()

    private val _results = MutableLiveData<List<Track>>()

    private var searchJob: Job? = null

    init {
        val searchHistory = searchHistoryInteractor.getSearchHistory()
        _searchHistory.postValue(ArrayDeque(searchHistory))
        LogUtil.d(LOG_TAG, "SearchHistory: ${_searchHistory.value}")
    }

    fun onQueryChanged(newQuery: String) {
        LogUtil.d(LOG_TAG, "onQueryChanged: $newQuery")

//        when {
//            newQuery.isBlank() && searchHistory.isNotEmpty() -> {
//                Debouncer.cancel(searchJob)
//                _uiState.postValue(SearchUiState.History(searchHistory.toList()))
//            }
//
//            newQuery.isBlank() && searchHistory.isEmpty() -> {
//                Debouncer.cancel(searchJob)
//                _uiState.postValue(SearchUiState.Idle)
//            }
//
//            searchQuery.isNotEmpty() && (searchQuery == newQuery) -> {
//                _uiState.postValue(SearchUiState.Content(results))
//            }
//
//            searchQuery.isNotEmpty() && (searchQuery != newQuery) -> {
//                Debouncer.debounce(action = searchJob)
//            }
//        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(2000L)
            performSearch()
        }

        _searchQuery.value = newQuery
    }

    private suspend fun performSearch() {
        if (searchQuery.isEmpty()) return

        _uiState.postValue(SearchUiState.Loading)

        searchTracksUseCase.execute(searchQuery).collect { (tracks, message) ->
            val newState = when {
                tracks == null -> {
                    LogUtil.e(LOG_TAG, "performSearch error: $message")
                    SearchUiState.Error
                }

                tracks.isEmpty() -> {
                    _results.postValue(tracks)
                    SearchUiState.NoResults
                }

                else -> {
                    _results.postValue(tracks)
                    SearchUiState.Content(tracks)
                }
            }

            _uiState.postValue(newState)
        }
    }

    fun onTrackClicked(track: Track) {
        val newTracks = ArrayDeque(searchHistory)

        if (track in newTracks) {
            newTracks.remove(track)
            newTracks.addFirst(track)
        } else if (newTracks.size < MAX_HISTORY_SIZE) {
            newTracks.addFirst(track)
        } else {
            newTracks.removeLast()
            newTracks.addFirst(track)
        }

        _searchHistory.postValue(newTracks)
        LogUtil.d(LOG_TAG, "SearchHistory: ${_searchHistory.value}")
    }

    fun onClearSearchHistoryClicked() {
        _searchHistory.postValue(ArrayDeque())
        _uiState.postValue(SearchUiState.Idle)
    }

    fun onClearQueueClicked() {
        _results.postValue(emptyList())
        _searchQuery.postValue("")
        _uiState.postValue(SearchUiState.Idle)
    }

    fun onSearchClicked() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            performSearch()
        }
    }

    fun onRetryClicked() {
        searchJob = viewModelScope.launch {
            performSearch()
        }
    }

    fun onPause() {
        searchHistoryInteractor.saveHistory(searchHistory)
    }

    override fun onCleared() {
        super.onCleared()
        searchHistoryInteractor.saveHistory(searchHistory)
    }

    companion object {
        private const val LOG_TAG = "SearchViewModel"
        private const val MAX_HISTORY_SIZE = 10
    }
}