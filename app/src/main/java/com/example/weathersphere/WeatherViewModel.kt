package com.example.weathersphere

import android.util.Log
import androidx.compose.ui.text.resolveDefaults
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersphere.api.Constant
import com.example.weathersphere.api.NetworkResponse
import com.example.weathersphere.api.RetrofitInstance
import com.example.weathersphere.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {
    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    //whenever we will search will get data from search model
    fun getData(city : String) {

        _weatherResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apikey , city)

                if(response.isSuccessful)
                {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                }
                else{
                    _weatherResult.value = NetworkResponse.Error("F A I L E D  T O  L O A D  D A T A")
                }
            }
            catch (e : Exception){
                _weatherResult.value = NetworkResponse.Error("F A I L E D  T O  L O A D  D A T A")
            }

        }
        //wrapiing weatherapi.getweather..... in w=viewModelScope as it takes time to load
        //Log.i("Response :" , response.body().toString())
        //Log.i("Error" , response.message())
    }
}