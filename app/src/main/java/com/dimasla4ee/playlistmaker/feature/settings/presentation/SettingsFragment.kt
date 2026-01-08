package com.dimasla4ee.playlistmaker.feature.settings.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.utils.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentSettingsBinding
import com.dimasla4ee.playlistmaker.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isDarkThemeEnabled.observe(viewLifecycleOwner) { isDarkTheme ->
            binding.themeSwitch.isChecked = isDarkTheme
            setAppTheme(isDarkTheme)
        }

        setupListeners()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    fun setAppTheme(useDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (useDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun setupListeners() {
        with(binding) {
            themeSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.onThemeToggle(checked)
            }

            shareButton.setOnClickListener {
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

            supportButton.setOnClickListener {
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

            agreementButton.setOnClickListener {
                val agreementIntent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = getString(R.string.user_agreement_link).toUri()
                }

                startActivity(agreementIntent)
            }
        }
    }
}