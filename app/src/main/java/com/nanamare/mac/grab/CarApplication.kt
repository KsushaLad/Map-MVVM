package com.nanamare.mac.grab

import android.app.Application
import com.nanamare.mac.grab.base.ui.BaseExceptionHandler
import com.nanamare.mac.grab.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CarApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@CarApplication)
            modules(
                listOf(
                    networkModule,
                    dataSourceModel,
                    viewModelModule,
                    apiModule,
                    prefUtilsModule
                )
            )
        }

        setDefaultHandler()
    }

    private fun setDefaultHandler() {
        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->

        }
        Thread.setDefaultUncaughtExceptionHandler(
            BaseExceptionHandler(this, defaultExceptionHandler)
        )
    }

}