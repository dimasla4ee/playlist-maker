package com.dimasla4ee.playlistmaker.feature.new_playlist.presentation

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil3.load
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.presentation.util.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.dimasla4ee.playlistmaker.feature.new_playlist.presentation.viewmodel.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment(R.layout.fragment_new_playlist) {

    private val binding by viewBinding(FragmentNewPlaylistBinding::bind)
    private val viewModel: NewPlaylistViewModel by viewModel()

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onCoverChanged(uri) }
    }

    private val onDoneDialogClick = DialogInterface.OnClickListener { _, _ ->
        findNavController().popBackStack()
    }

    private lateinit var dialog: MaterialAlertDialogBuilder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.exit_confirmation_title)
            .setMessage(R.string.exit_confirmation_message)
            .setNeutralButton(R.string.dismiss, null)
            .setPositiveButton(R.string.done, onDoneDialogClick)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners(): Unit = with(binding) {
        appBar.setNavigationOnClickListener {
            viewModel.onBackButtonPressed()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackButtonPressed()
        }

        playlistName.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text.toString())
        }

        playlistDescription.doOnTextChanged { text, _, _, _ ->
            viewModel.onDescriptionChanged(text.toString())
        }

        createButton.setOnClickListener {
            viewModel.onCreatePlaylist()
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.playlist_created, viewModel.name.value),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }

        playlistCover.setOnClickListener {
            val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            val request = PickVisualMediaRequest(mediaType)
            pickMedia.launch(request)
        }
    }

    private fun setupObservers(): Unit = with(binding) {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.name.collect { name ->
                        val isCreationAllowed = name.isNotBlank()
                        createButton.isEnabled = isCreationAllowed
                    }
                }

                launch {
                    viewModel.cover.collect { uri ->
                        val (data, scaleType) = when (uri) {
                            Uri.EMPTY -> R.drawable.ic_add_photo_100 to ImageView.ScaleType.CENTER
                            else -> uri to ImageView.ScaleType.CENTER_CROP
                        }
                        val radius = resources.getDimension(R.dimen.coverCornerRadius)

                        playlistCover.load(data) {
                            transformations(RoundedCornersTransformation(radius))
                            crossfade(true)
                            playlistCover.scaleType = scaleType
                        }
                    }
                }

                launch {
                    viewModel.showExitConfirmation.collect { event ->
                        when (event) {
                            NavigationEvent.PopBackStack -> findNavController().popBackStack()
                            NavigationEvent.ShowExitConfirmation -> dialog.show()
                        }
                    }
                }
            }

        }
    }

}