package com.dimasla4ee.playlistmaker.feature.settings.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.presentation.util.setupWindowInsets
import com.dimasla4ee.playlistmaker.databinding.ActivitySettingsBinding
import com.dimasla4ee.playlistmaker.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater).apply {
            setContentView(root)
            enableEdgeToEdge()
            root.setupWindowInsets()
        }

        viewModel.isDarkThemeEnabled.observe(this) { isDarkTheme ->
            binding.darkThemeSwitch.isChecked = isDarkTheme
            setAppTheme(isDarkTheme)
        }

        setupListeners()
    }

    fun setAppTheme(useDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (useDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun setupListeners() {
        with(binding) {
            panelHeader.setOnIconClickListener {
                finish()
            }

            darkThemeLayout.setOnClickListener {
                darkThemeSwitch.isChecked = !darkThemeSwitch.isChecked
            }

            darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.onThemeToggle(checked)
            }

            shareAppContainer.setOnClickListener {
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

            textSupportContainer.setOnClickListener {
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

            userAgreementContainer.setOnClickListener {
                val agreementIntent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = getString(R.string.user_agreement_link).toUri()
                }

                startActivity(agreementIntent)
            }
        }
    }
}