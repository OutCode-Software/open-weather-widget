package app.weather

import android.Manifest
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import app.common.extension.checkLocationPermission
import app.common.extension.logger
import app.weather.services.WeatherWorker
import java.util.*
import java.util.concurrent.TimeUnit

const val accessFineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
const val accessCoarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
const val WEATHER_WIDGET_WORKER = "WEATHER_WIDGET_WORKER"
const val OPEN_WEATHER_ICON_PATH = "https://openweathermap.org/img/wn"

lateinit var locationManager: LocationManager

fun MainActivity.requestPermission() {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all {
            it.value
        }
    }.launch(
        arrayOf(
            accessFineLocationPermission,
            accessCoarseLocationPermission
        )
    )
}

fun MainActivity.initWeatherAndDashboardData() {
    if (checkLocationPermission()) {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.run {
            accuracy = Criteria.ACCURACY_FINE
        }
        val locationListener = LocationListener { location ->
            val fetchedLocation: Location = location
            logger("Fetched Location: " + fetchedLocation.latitude + ", " + fetchedLocation.longitude)
            if (prefs.currentLat.isNotEmpty() && prefs.currentLng.isNotEmpty()) {
                val storedLocation = Location("Cached User Location")
                storedLocation.latitude = prefs.currentLat.toDouble()
                storedLocation.longitude = prefs.currentLng.toDouble()
                logger("Stored Location: " + storedLocation.latitude + ", " + storedLocation.longitude)
                logger("Distance: (in metres) " + storedLocation.distanceTo(fetchedLocation))
                if (storedLocation.distanceTo(fetchedLocation) > 10000) {
                    prefs.currentLat = fetchedLocation.latitude.toString()
                    prefs.currentLng = fetchedLocation.longitude.toString()
                    initWorkManager()
                } else {
                    prefs.currentLat = fetchedLocation.latitude.toString()
                    prefs.currentLng = fetchedLocation.longitude.toString()
                }
            } else {
                prefs.currentLat = location.latitude.toString()
                prefs.currentLng = location.longitude.toString()
                if (!prefs.isWeatherFetched) initWorkManager()
            }
        }
        locationManager.requestSingleUpdate(criteria, locationListener, null)
    }
}

fun MainActivity.initWorkManager() {
    val periodicWork =
        PeriodicWorkRequestBuilder<WeatherWorker>(2, TimeUnit.HOURS)
            .build()
    val mWorkManager = WorkManager.getInstance(this)
    mWorkManager.enqueueUniquePeriodicWork(
        WEATHER_WIDGET_WORKER,
        ExistingPeriodicWorkPolicy.REPLACE,
        periodicWork
    )
}

fun MainActivity.getTimePeriod(c: Calendar): TimeOfDay {
    when (c.get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> {
            return TimeOfDay.Morning
        }
        in 12..15 -> {
            return TimeOfDay.Day
        }
        in 16..20 -> {
            return TimeOfDay.Evening
        }
        in 21..23 -> {
            return TimeOfDay.Night
        }
        else -> {
            return TimeOfDay.Morning
        }
    }
}

enum class TimeOfDay {
    Morning, Day, Evening, Night
}
