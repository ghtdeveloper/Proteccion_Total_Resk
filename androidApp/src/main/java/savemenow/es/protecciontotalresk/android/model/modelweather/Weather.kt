package savemenow.es.protecciontotalresk.android.model.modelweather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 ****Created by: Edison Martinez,
 ****Date:29,Monday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class Weather
{
    @SerializedName("main")
    @Expose
    lateinit var  main: String
    @SerializedName("description")
    @Expose
    lateinit var description : String
    @SerializedName("icon")
    @Expose
    lateinit var icon : String

}