package savemenow.es.protecciontotalresk.android.util

import java.sql.Timestamp


/**
 ****Created by: Edison Martinez,
 ****Date:21,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class DateTimeStamp {

    private var timestamp: Timestamp? = null

    init {
        timestamp = Timestamp(System.currentTimeMillis())
    }

    fun getDateTime() : Timestamp?
    {
        return timestamp
    }
}