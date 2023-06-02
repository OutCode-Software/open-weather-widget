package app.weather.di

import app.weather.data.Prefs
import app.weather.data.api.WeatherApi
import app.weather.di.DataSourceProperties.OPEN_WEATHER_URL
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import okhttp3.Interceptor

val netModule = module {
    single { provideGson() }
    factory { provideWeatherApi<WeatherApi>(OPEN_WEATHER_URL) }
}

object DataSourceProperties {
    const val OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/"
}

fun provideGson(): Gson = Gson()

inline fun <reified T> provideWeatherApi(url: String): T {
    val interceptor = Interceptor { chain ->
        var request = chain.request()
        request = request.newBuilder()
            .build()
        chain.proceed(request)
    }
    val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create()
}
