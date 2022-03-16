package com.amadydev.doggy

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.amadydev.doggy.ui.main.MainActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DoggyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        crashConfig()
    }


    private fun crashConfig() {
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
            .showErrorDetails(false)
            .minTimeBetweenCrashesMs(2000)
            .logErrorOnRestart(false)
            .errorDrawable(R.mipmap.ic_launcher)
            .restartActivity(MainActivity::class.java)
            .apply()
    }
}