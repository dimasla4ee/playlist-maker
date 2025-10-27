package com.dimasla4ee.playlistmaker.feature.media_library.presentation

import androidx.fragment.app.Fragment
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.presentation.util.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentFavoriteBinding
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.viewmodel.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding by viewBinding(FragmentFavoriteBinding::bind)
    private val viewModel: FavoriteViewModel by viewModel()

    companion object {

        fun newInstance() = FavoriteFragment()
    }
}