package savemenow.es.protecciontotalresk.android.apis.builder

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 ****Created by: Edison Martinez,
 ****Date:27,Saturday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
object ServiceBuilderWeather
{
    private val client = OkHttpClient.Builder().build()
    //retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    fun <T> buildService(service:Class<T>) : T{
        return retrofit.create(service)
    }
}