package com.dimasla4ee.playlistmaker.feature.favorite.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.presentation.util.show
import com.dimasla4ee.playlistmaker.core.presentation.util.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentFavoriteBinding
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.model.FavoriteUiState
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.viewmodel.FavoriteViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding by viewBinding(FragmentFavoriteBinding::bind)
    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    render(state)
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun render(state: FavoriteUiState) {
        when (state) {
            is FavoriteUiState.Content -> showContent(state)
            is FavoriteUiState.Empty -> showEmpty()
        }
    }

    private fun showContent(state: FavoriteUiState.Content) {
        binding.stateInfo.show(false)
        //TODO
    }

    private fun showEmpty() {
        binding.stateInfo.show(true)
        //TODO
    }

    companion object {

        fun newInstance() = FavoriteFragment()
    }
}