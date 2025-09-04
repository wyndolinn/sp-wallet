package com.wynndie.spwallet

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.wynndie.spwallet.sharedCore.domain.repository.DataStoreRepository
import com.wynndie.spwallet.sharedFeature.profile.domain.model.LocalizationController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val dataStoreRepository: DataStoreRepository by inject()
    private val localizationController: LocalizationController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        super.onCreate(savedInstanceState)

        println("PRINTLINE: ON CREATE")

        lifecycleScope.launch {
            val storedLanguageIso = dataStoreRepository.getLocalization().first()
            localizationController.applyLanguage(storedLanguageIso)
        }

        setContent {
            App()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        println("PRINTLINE: CONFIG CHANGE")

        lifecycleScope.launch {
            val storedLanguageIso = dataStoreRepository.getLocalization().first()
            localizationController.applyLanguage(storedLanguageIso)
        }
    }
}