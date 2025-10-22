package com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dimasla4ee.playlistmaker.app.creator.Creator
import com.dimasla4ee.playlistmaker.core.domain.consumer.Consumer
import com.dimasla4ee.playlistmaker.core.domain.consumer.ConsumerData
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.util.Debouncer
import com.dimasla4ee.playlistmaker.core.util.LogUtil
import com.dimasla4ee.playlistmaker.feature.search.domain.usecase.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.SearchActivityState

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _uiState = MutableLiveData<SearchActivityState>(SearchActivityState.Content)
    val uiState: LiveData<SearchActivityState>
        get() = _uiState

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String>
        get() = _searchQuery
    private val isSearchQueryBlank: Boolean
        get() = _searchQuery.value.isNullOrBlank()

    private val _searchHistory = MutableLiveData<ArrayDeque<Track>>(ArrayDeque())
    val searchHistory: LiveData<ArrayDeque<Track>>
        get() = _searchHistory
    private val isSearchHistoryEmpty: Boolean
        get() = _searchHistory.value.isNullOrEmpty()

    private val _results = MutableLiveData<List<Track>>()
    val results: LiveData<List<Track>>
        get() = _results

    private val searchRunnable = Runnable { performSearch() }
    private val searchTracksUseCase = Creator.provideSearchTracksUseCase()

    init {
        searchHistoryInteractor.getSearchHistory(
            object : SearchHistoryInteractor.HistoryConsumer {
                override fun consume(searchHistory: List<Track>) {
                    _searchHistory.postValue(ArrayDeque(searchHistory))
                }
            }
        )
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery

        if (newQuery.isBlank()) {
            Debouncer.cancel(searchRunnable)
            if (isSearchHistoryEmpty) {
                _uiState.postValue(SearchActivityState.Content)
            } else {
                _uiState.postValue(SearchActivityState.SearchHistory)
            }
            return
        }

        Debouncer.debounce(action = searchRunnable)
    }

    private fun performSearch() {
        val currentQuery = _searchQuery.value ?: return
        if (isSearchQueryBlank) return

        _uiState.postValue(SearchActivityState.Loading)

        searchTracksUseCase.execute(
            query = currentQuery,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    val newState = when (data) {
                        is ConsumerData.Data -> {
                            val tracks = data.value
                            _results.postValue(tracks)

                            if (tracks.isEmpty()) {
                                SearchActivityState.NoResults
                            } else {
                                SearchActivityState.Content
                            }
                        }

                        is ConsumerData.Error -> SearchActivityState.Error
                    }
                    _uiState.postValue(newState)
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
    }

    fun onClearSearchHistoryClicked() {
        _searchHistory.postValue(ArrayDeque())
    }

    fun onClearQueueClicked() {
        _results.postValue(listOf())
        _searchQuery.postValue("")
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
            LogUtil.d("onFocusChanged", "SearchHistory")
            SearchActivityState.SearchHistory
        } else {
            LogUtil.d("onFocusChanged", "Content")
            SearchActivityState.Content
        }
        _uiState.postValue(newState)
    }

    override fun onCleared() {
        super.onCleared()
        searchHistoryInteractor.saveHistory(_searchHistory.value ?: listOf())
    }

    companion object {

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as Application
                SearchViewModel(
                    Creator.provideSearchHistoryInteractor(app)
                )
            }
        }

        private const val MAX_HISTORY_SIZE = 10
    }
}