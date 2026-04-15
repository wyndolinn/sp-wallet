package com.wynndie.spwallet

import android.app.Application
import com.wynndie.spwallet.di.initKoin
import org.koin.android.ext.koin.androidContext

class SpWalletApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@SpWalletApp)
        }
    }
}