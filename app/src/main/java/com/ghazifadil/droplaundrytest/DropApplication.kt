package com.ghazifadil.droplaundrytest

import android.app.Application
import com.ghazifadil.droplaundrytest.common.*
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class DropApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DropApplication)
            modules(
                apiModule,
                viewModelModule,
                repositoryModule,
                networkModule
            )
        }
    }

}