package com.project.prayerreminder

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.route.AppRoute
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase.createLanguageContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrayerReminderTheme {
                AppRoute(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    private fun Context.createLanguageContext(): Context {
        val language = (applicationContext as PrayerApplication).currentLanguage
        val locale = Locale.forLanguageTag(language.languageTag)
        Locale.setDefault(locale)

        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        return createConfigurationContext(configuration)
    }
}