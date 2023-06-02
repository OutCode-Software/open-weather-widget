package app

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import app.common.extension.plantLogger
import app.weather.data.Prefs
import app.weather.di.appModule
import app.weather.di.netModule
import app.weather.di.storageModule
import app.weather.di.workerModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App : Application(), LifecycleObserver {
    companion object {
        lateinit var instance: App
    }

    private val pref: Prefs by inject()
    override fun onCreate() {
        super.onCreate()
        instance = this
        plantLogger()
        startKoin {
            androidLogger()
            androidContext(this@App)
            workManagerFactory()
            koin.loadModules(
                listOf(
                    appModule,
                    storageModule,
                    netModule,
                    workerModule
                )
            )
        }
    }

}