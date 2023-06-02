package app.weather.data.api

import app.weather.data.response.DailyWeatherResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    companion object {
        private const val FORECAST = "forecast/daily"
    }

    @GET(FORECAST)
    fun getDailyForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") cnt: Int = 7,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String
    ): Call<ResponseBody>
}