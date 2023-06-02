package app.weather.services

import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import app.common.extension.loggerE
import app.common.extension.orUnknownError
import app.weather.data.Prefs
import app.weather.data.api.WeatherApi
import app.weather.data.response.BaseErrorEntity
import app.weather.data.response.DailyWeatherResponse
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

interface MyCallback {
    fun onSuccess()
    fun onError()
}

class WeatherWorker(val context: Context, private val workerParams: WorkerParameters) :
    ListenableWorker(context, workerParams), KoinComponent {
    private val prefs: Prefs by inject()
    private val api: WeatherApi by inject()

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture {
            val callback = object : MyCallback {
                override fun onSuccess() {
                    it.set(Result.success())
                }

                override fun onError() {
                    it.set(Result.failure())
                }
            }
            fetchWeatherApi(callback)
            return@getFuture callback
        }
    }

    private fun fetchWeatherApi(callback: MyCallback) {
        api.getDailyForecast(
            prefs.currentLat.toDouble(),
            prefs.currentLng.toDouble(),
            appId = prefs.openWeatherAppId
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    when {
                        response.isSuccessful -> {
                            prefs.isWeatherFetched = true
                            loggerE("Hit Weather Api")
                            response.body()?.let {
                                prefs.dailyWeatherResponse =
                                    Gson().fromJson(it.string(), DailyWeatherResponse::class.java)
                            }
                            callback.onSuccess()
                        }
                        response.code() in listOf(401, 403) -> {
                            prefs.isWeatherFetched = false
                            callback.onError()
                            val errorBody = response.errorBody()?.string()
                            val errorModel = Gson().fromJson(errorBody, BaseErrorEntity::class.java)
                            loggerE(errorModel.message ?: errorModel.detail)
                        }
                        response.code() in listOf(500, 404) -> {
                            prefs.isWeatherFetched = false
                            callback.onError()
                            loggerE("Server Error")
                        }
                        else -> {
                            prefs.isWeatherFetched = false
                            callback.onError()
                            loggerE("Other Error")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    prefs.isWeatherFetched = false
                    callback.onError()
                    Timber.i(t.localizedMessage.orUnknownError())
                }

            })
    }
}