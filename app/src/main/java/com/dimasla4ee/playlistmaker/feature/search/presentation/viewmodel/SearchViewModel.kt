package com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.utils.LogUtil
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchTracksUseCase
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.SearchUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    /**
     * Represents a search request initiated by the user.
     *
     * @property term The search query string.
     * @property instant A flag indicating whether the search should be executed immediately,
     * bypassing any debounce logic.
     */
    private data class SearchRequest(
        val term: String,
        val instant: Boolean = false
    )

    private val query = MutableStateFlow("")

    private val immediateFlow = MutableSharedFlow<SearchRequest>(
        replay = 1,
        extraBufferCapacity = 1
    )

    private val debouncedQueryFlow = query
        .debounce(SEARCH_DELAY_MS)
        .distinctUntilChanged { old, new -> old.trim() == new.trim() }
        .map { SearchRequest(it, instant = false) }

    private val searchHistoryFlow = MutableStateFlow(
        ArrayDeque(searchHistoryInteractor.getSearchHistory())
    )
    private val searchHistory get() = searchHistoryFlow.value

    val uiState: StateFlow<SearchUiState> = merge(
        immediateFlow,
        debouncedQueryFlow
    )
        .distinctUntilChanged { old, new ->
            val areEquivalent = when {
                old.term != new.term -> false
                !old.instant && !new.instant -> true
                old.instant && !new.instant -> true
                else -> false
            }

            return@distinctUntilChanged areEquivalent
        }
        .flatMapLatest { request ->
            LogUtil.d(LOG_TAG, "SearchRequest: term='${request.term}', manual=${request.instant}")

            if (request.term.isBlank()) {
                val state = when {
                    searchHistory.isEmpty() -> SearchUiState.Idle
                    else -> SearchUiState.History(searchHistory)
                }
                return@flatMapLatest flowOf(state)
            }

            flow {
                emit(SearchUiState.Loading)

                val (tracks, message) = try {
                    searchTracksUseCase.execute(request.term).first()
                } catch (t: Throwable) {
                    LogUtil.e(LOG_TAG, "search exception: ${t.message}")
                    emit(SearchUiState.Error)
                    return@flow
                }

                val newState = when {
                    tracks == null -> SearchUiState.Error
                    tracks.isEmpty() -> SearchUiState.NoResults
                    else -> SearchUiState.Content(tracks)
                }

                emit(newState)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = if (searchHistory.isEmpty()) {
                SearchUiState.Idle
            } else {
                SearchUiState.History(searchHistory)
            }
        )

    fun onQueryChanged(newQuery: String) {
        LogUtil.d(LOG_TAG, "onQueryChanged: $newQuery")
        query.update { newQuery }
    }

    fun onSearchClicked() {
        viewModelScope.launch {
            immediateFlow.emit(SearchRequest(query.value, instant = true))
        }
    }

    fun onRetryClicked() {
        viewModelScope.launch {
            immediateFlow.emit(SearchRequest(query.value, instant = true))
        }
    }

    fun onClearQueueClicked() {
        query.update { "" }
        viewModelScope.launch {
            immediateFlow.emit(SearchRequest("", instant = true))
        }
    }

    fun onClearSearchHistoryClicked() {
        searchHistoryFlow.update { ArrayDeque() }
        viewModelScope.launch {
            immediateFlow.emit(SearchRequest("", instant = true))
        }
    }

    fun onTrackClicked(track: Track) {
        val updatedHistory = ArrayDeque(searchHistory)
        if (track in updatedHistory) {
            updatedHistory.remove(track)
        } else if (updatedHistory.size >= MAX_HISTORY_SIZE) {
            updatedHistory.removeLast()
        }
        updatedHistory.addFirst(track)

        searchHistoryFlow.update { updatedHistory }
        LogUtil.d(LOG_TAG, "SearchHistory: ${searchHistoryFlow.value}")

        viewModelScope.launch {
            immediateFlow.emit(SearchRequest(query.value, instant = true))
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
        private const val SEARCH_DELAY_MS = 2000L
    }
}
