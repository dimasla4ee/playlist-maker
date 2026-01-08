package com.dimasla4ee.playlistmaker.feature.favorite.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.adapter.TrackAdapter
import com.dimasla4ee.playlistmaker.core.utils.debounce
import com.dimasla4ee.playlistmaker.core.utils.show
import com.dimasla4ee.playlistmaker.core.utils.viewBinding
import com.dimasla4ee.playlistmaker.core.utils.LogUtil
import com.dimasla4ee.playlistmaker.databinding.FragmentFavoriteBinding
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.model.FavoriteUiState
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.viewmodel.FavoriteViewModel
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.MediaLibraryFragmentDirections
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding by viewBinding(FragmentFavoriteBinding::bind)
    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var recyclerAdapter: TrackAdapter
    private lateinit var onTrackClickedDebounce: (Track) -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    render(state)
                }
            }
        }

        onTrackClickedDebounce = debounce(
            delayMillis = 300L,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { track ->
            findNavController().navigate(
                MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlayerFragment(track)
            )
        }

        recyclerAdapter = TrackAdapter { onItemClick(it) }
        binding.recycler.adapter = recyclerAdapter
    }

    private fun onItemClick(track: Track) {
        onTrackClickedDebounce(track)
    }

    private fun render(state: FavoriteUiState) {
        LogUtil.d(LOG_TAG, "render() called with: state = $state")

        when (state) {
            is FavoriteUiState.Content -> showContent(state)
            is FavoriteUiState.Empty -> showEmpty()
            is FavoriteUiState.Idle -> showIdle()
        }
    }

    private fun showContent(state: FavoriteUiState.Content) {
        recyclerAdapter.submitList(state.tracks)
        with(binding) {
            recycler.show(true)
            stateInfo.show(false)
        }
    }

    private fun showIdle() {
        with(binding) {
            recycler.show(false)
            stateInfo.show(false)
        }
    }

    private fun showEmpty() {
        with(binding) {
            recycler.show(false)
            stateInfo.show(true)
        }
    }

    companion object {
        private const val LOG_TAG = "FavoriteFragment"
        fun newInstance() = FavoriteFragment()
    }

}