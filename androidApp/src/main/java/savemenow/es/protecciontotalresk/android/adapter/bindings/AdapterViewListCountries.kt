package savemenow.es.protecciontotalresk.android.adapter.bindings

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import savemenow.es.protecciontotalresk.android.R


/**
 ****Created by: Edison Martinez,
 ****Date:28,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class AdapterViewListCountries(val view: View) : RecyclerView.ViewHolder(view)
{
    val imgFlagCountry : ImageView = view.findViewById(R.id.imgFlagCountry)
    val tvCountryName : TextView = view.findViewById(R.id.tvCountryName)
    val cardViewListCountrie : CardView = view.findViewById(R.id.cardViewListCountrie)

}