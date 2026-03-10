package com.dimasla4ee.playlistmaker.feature.settings.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.app.ui.theme.PlaylistMakerTheme
import com.dimasla4ee.playlistmaker.feature.settings.presentation.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val isDarkThemeEnabled by viewModel.isDarkThemeEnabled.collectAsState()
                var localChecked by remember { mutableStateOf(isDarkThemeEnabled) }

                LaunchedEffect(isDarkThemeEnabled) {
                    if (localChecked != isDarkThemeEnabled) {
                        localChecked = isDarkThemeEnabled
                    }

                    delay(100)
                    setAppTheme(isDarkThemeEnabled)
                }

                PlaylistMakerTheme {
                    SettingsPane(
                        modifier = Modifier.fillMaxSize(),
                        isDarkTheme = localChecked,
                        onThemeToggle = { isChecked ->
                            localChecked = isChecked
                            viewModel.onThemeToggle(isChecked)
                        },
                        onShareClick = { shareIntent() },
                        onSupportClick = { emailIntent() },
                        onAgreementClick = { agreementIntent() }
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun shareIntent() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.yandex_practicum_course_link)
            )
            type = getString(R.string.plain_text_intent_type)
        }

        startActivity(shareIntent)
    }

    private fun emailIntent() {
        val emailIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = getString(R.string.mailto_uri_data).toUri()
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.developer_email))
            )
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.letter_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.letter_text))
        }

        startActivity(emailIntent)
    }

    private fun agreementIntent() {
        val agreementIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = getString(R.string.user_agreement_link).toUri()
        }

        startActivity(agreementIntent)
    }

    private fun setAppTheme(useDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (useDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}
