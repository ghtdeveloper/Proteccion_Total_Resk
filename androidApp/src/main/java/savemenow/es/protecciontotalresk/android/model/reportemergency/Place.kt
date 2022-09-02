package savemenow.es.protecciontotalresk.android.model.reportemergency

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


/**
 ****Created by: Edison Martinez,
 ****Date:31,Wednesday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
data class Place(
    val name: String,
    val latLng: LatLng,
    val address: String,
    val rating: Float
) : ClusterItem {
    override fun getPosition(): LatLng =
        latLng

    override fun getTitle(): String =
        name

    override fun getSnippet(): String =
        address
}
