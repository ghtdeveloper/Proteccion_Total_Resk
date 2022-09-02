package savemenow.es.protecciontotalresk.android.model.User

import java.util.*


/**
 ****Created by: Edison Martinez,
 ****Date:21,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
data class User(
     val address: String? = null,
     val company : String? = null,
     val country : String? = null,
     val email : String? = null,
     val fullName: String? = null,
     val id : String? = null,
     val pass :String? = null,
     val phone : String? = null,
     val providerType : String? = null,
     val regDate :Date? = null,
     val uriPhoto : String? = null
)
