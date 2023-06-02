@file:Suppress("NOTHING_TO_INLINE")

package app.common.extension

import timber.log.Timber
import com.outcode.weather_android_reusable.BuildConfig


inline fun logger(msg: String?) {
    if (BuildConfig.DEBUG) Timber.d(msg)
}

inline fun loggerE(msg: String?) {
    if (BuildConfig.DEBUG) Timber.e(msg)
}

inline fun logger(msg: String?, tag: String) {
    if (BuildConfig.DEBUG) Timber.tag(tag).d(msg)
}

inline fun plantLogger() {
    if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    }
}