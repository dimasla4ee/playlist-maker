package com.dimasla4ee.playlistmaker.feature.media_library.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dimasla4ee.playlistmaker.databinding.FragmentFavoriteBinding
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.viewmodel.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = FavoriteFragment()
    }
}