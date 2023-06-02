package app.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.common.extension.checkLocationPermissionDenied
import app.common.extension.logger
import app.loadWeatherData
import app.weather.data.Prefs
import org.koin.android.ext.android.inject
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    val prefs: Prefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        initWeatherAndDashboardData()
        setContent {
            getWeather()
        }
    }

    fun setWeatherApiRapidKey(key: String) {
        prefs.openWeatherApiRapidKey = key
    }

    fun setWeatherApiId(key: String) {
        prefs.openWeatherAppId = key
    }

    @Preview
    @Composable
    fun getWeather() {
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Top) {
            prefs.dailyWeatherResponse?.let { weatherData ->
                val date = java.util.Calendar.getInstance()
                val listFirst = weatherData.list.first()
                val temperature =
                    when (getTimePeriod(date)) {
                        TimeOfDay.Morning -> "${listFirst.feelsLike.morn.roundToInt()}°F"
                        TimeOfDay.Day -> "${listFirst.feelsLike.day.roundToInt()}°F"
                        TimeOfDay.Evening -> "${listFirst.feelsLike.eve.roundToInt()}°F"
                        TimeOfDay.Night -> "${listFirst.feelsLike.night.roundToInt()}°F"
                    }
                val city = weatherData.city.name
                val description = listFirst.weather.first().description
                val image = "$OPEN_WEATHER_ICON_PATH/${listFirst.weather.first().icon}@4x.png"
                logger("$temperature, $city, $description, $image")
                loadWeatherData(
                    temperature = temperature,
                    city = city,
                    description = description,
                    image = image
                )
            }
            HandlePermissionsDeclined()
        }
    }

    @Composable
    fun HandlePermissionsDeclined() {
        if (checkLocationPermissionDenied()) {
            Snackbar(content = { Text(text = "Permission Declined") })
        }
    }

}