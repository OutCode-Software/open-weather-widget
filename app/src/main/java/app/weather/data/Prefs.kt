package app.weather.data

import android.content.SharedPreferences
import androidx.core.content.edit
import app.weather.data.response.DailyWeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Prefs(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val LAT_LNG_DATA = "LAT_LNG_DATA"
        private const val RESPONSE_WEATHER = "RESPONSE_WEATHER"
        private const val CURRENT_LAT = "CURRENT_LAT"
        private const val CURRENT_LNG = "CURRENT_LNG"
        private const val KEY_IS_WORKER_STARTED = "KEY_IS_WORKER_STARTED"
        private const val KEY_OPEN_API_RAPID = "KEY_OPEN_API_RAPID"
        private const val KEY_OPEN_API_APP_ID = "KEY_OPEN_API_APP_ID"
    }

    val gson = Gson()

    var tmpLocation: String
        get() = sharedPreferences.getString(LAT_LNG_DATA, "") ?: ""
        set(value) = sharedPreferences.edit {
            putString(LAT_LNG_DATA, value)
        }

    var currentLat: String
        get() = sharedPreferences.getString(CURRENT_LAT, "") ?: ""
        set(value) = sharedPreferences.edit {
            putString(CURRENT_LAT, value)
        }

    var currentLng: String
        get() = sharedPreferences.getString(CURRENT_LNG, "") ?: ""
        set(value) = sharedPreferences.edit {
            putString(CURRENT_LNG, value)
        }

    var isWeatherFetched: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_WORKER_STARTED, false)
        set(value) = sharedPreferences.edit {
            putBoolean(KEY_IS_WORKER_STARTED, value)
        }

    var dailyWeatherResponse: DailyWeatherResponse?
        get() {
            val jsonString = sharedPreferences.getString(RESPONSE_WEATHER, null)
            return Gson().fromJson(
                jsonString ?: return null,
                object : TypeToken<DailyWeatherResponse>() {}.type
            )
        }
        set(value) = sharedPreferences.edit {
            putString(RESPONSE_WEATHER, gson.toJson(value))
        }

    var openWeatherApiRapidKey: String
        get() = sharedPreferences.getString(KEY_OPEN_API_RAPID, "") ?: ""
        set(value) = sharedPreferences.edit {
            putString(KEY_OPEN_API_RAPID, value)
        }

    var openWeatherAppId: String
        get() = sharedPreferences.getString(KEY_OPEN_API_APP_ID, "") ?: ""
        set(value) = sharedPreferences.edit {
            putString(KEY_OPEN_API_APP_ID, value)
        }

    fun clearPrefs() = sharedPreferences.edit().clear().commit()


}