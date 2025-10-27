package com.dimasla4ee.playlistmaker.feature.media_library.presentation

import androidx.fragment.app.Fragment
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.presentation.util.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentPlaylistBinding
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private val binding by viewBinding(FragmentPlaylistBinding::bind)
    private val viewModel: PlaylistsViewModel by viewModel()

    companion object {

        fun newInstance() = PlaylistFragment()
    }
}