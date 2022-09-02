package savemenow.es.protecciontotalresk.android.adapter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.model.reportemergency.EmergencyInfo
import savemenow.es.protecciontotalresk.android.model.reportemergency.Place


/**
 ****Created by: Edison Martinez,
 ****Date:31,Wednesday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class MarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter
{
    override fun getInfoContents(marker: Marker): View?
    {
        val place = marker?.tag as? Place ?: return null
        //Inflate view an set info
        val view = LayoutInflater.from(context).inflate(R.layout.marker_info_loc_user,null)
        view.findViewById<TextView>(R.id.text_view_title)
            .text = place.name
        view.findViewById<TextView>(
            R.id.text_view_address
        ).text = place.address
        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }


}