package com.mankahla.weatherappkotlin.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mankahla.weatherappkotlin.api.WeatherAPIClient
import com.mankahla.weatherappkotlin.data.CombineResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val apiClient = WeatherAPIClient()
    private val disposable = CompositeDisposable()

    val combineResponseData = MutableLiveData<CombineResponse>()
    val loadingIndicator = MutableLiveData<Boolean>()

    fun getForecastData(latitude: String, longitude: String, units: String) {
        loadingIndicator.value = true
        disposable.add(apiClient.getCombinedResponse(latitude, longitude, units)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<CombineResponse>() {
                override fun onSuccess(combinedResponse: CombineResponse) {
                    loadingIndicator.value = false
                    combineResponseData.value = combinedResponse
                }

                override fun onError(e: Throwable) {
                    Log.i("Error : ", e.message + " " + e.printStackTrace())
                }

            }))
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}