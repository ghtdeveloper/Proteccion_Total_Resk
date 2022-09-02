package savemenow.es.protecciontotalresk.android.model.reportemergency

import com.google.firebase.firestore.GeoPoint
import java.util.*


/**
 ****Created by: Edison Martinez,
 ****Date:30,Tuesday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
data class EmergencyInfo(
    val id : String? = null,
    val type : String? = null,
    val reportingUser : String? = null,
    val addressReported : String? = null,
    val reportedDate : Date? = null,
    val geoPoint: GeoPoint? = null,
    val status : String? = null
)
