package savemenow.es.protecciontotalresk.android.model.modelweather

import com.google.gson.annotations.SerializedName


/**
 ****Created by: Edison Martinez,
 ****Date:29,Monday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class WeatherInfo
{
    //Objetcts
    @SerializedName("weather")
    lateinit var weather : List<Weather>
    @SerializedName("main")
    lateinit var temperature: Temperature
    @SerializedName("cod")
    var cod:Int = 0
    @SerializedName("name")
    lateinit var name : String

    fun getArrayWeather() : List<Weather>
    {
        return weather
    }


}