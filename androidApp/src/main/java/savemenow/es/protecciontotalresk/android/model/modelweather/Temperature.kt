package savemenow.es.protecciontotalresk.android.model.modelweather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 ****Created by: Edison Martinez,
 ****Date:29,Monday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class Temperature
{
    @SerializedName("temp")
    @Expose
    var  temp: Double = 0.0

}