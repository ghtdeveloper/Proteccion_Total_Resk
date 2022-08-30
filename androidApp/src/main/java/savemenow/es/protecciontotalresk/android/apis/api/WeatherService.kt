package savemenow.es.protecciontotalresk.android.apis.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import savemenow.es.protecciontotalresk.android.model.modelweather.WeatherInfo


/**
 ****Created by: Edison Martinez,
 ****Date:29,Monday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
interface WeatherService
{
    @GET("data/2.5/weather?")
    fun getInfoWeather(@Query("lat")lat:Double,
                       @Query("lon")lon : Double,
                       @Query("APPID") appID:String,
                       @Query("lang") lang:String,
                       @Query("units") units:String) : Call<WeatherInfo>

}