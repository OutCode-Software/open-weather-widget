package app.weather.di

import android.content.Context
import android.content.SharedPreferences
import app.weather.data.Prefs
import app.weather.di.PersistenceDataSourceProperties.PREF_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val storageModule = module {
    single { PREF_NAME.provideSharedPreference(get()) }
    single { providePrefsManager(get()) }
}

object PersistenceDataSourceProperties {
    const val PREF_NAME = "weather.widget.preference"
}

private fun String.provideSharedPreference(context: Context): SharedPreferences {
    return context.getSharedPreferences(this, Context.MODE_PRIVATE)
}

private fun providePrefsManager(sharedPreferences: SharedPreferences): Prefs {
    return Prefs(sharedPreferences)
}