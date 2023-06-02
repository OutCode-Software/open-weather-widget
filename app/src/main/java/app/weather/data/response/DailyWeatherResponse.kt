package app.weather.data.response


import app.esgonsite.data.response.weather.City
import app.esgonsite.data.response.weather.Data
import com.google.gson.annotations.SerializedName

data class DailyWeatherResponse(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("list")
    val list: List<Data>,
    @SerializedName("message")
    val message: Double
)