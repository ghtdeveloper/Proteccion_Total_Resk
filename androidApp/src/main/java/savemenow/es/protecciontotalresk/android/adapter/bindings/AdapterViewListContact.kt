package savemenow.es.protecciontotalresk.android.adapter.bindings

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import savemenow.es.protecciontotalresk.android.R


/**
 ****Created by: Edison Martinez,
 ****Date:23,Tuesday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class AdapterViewListContact(itemview: View) : RecyclerView.ViewHolder(itemview) {
    //Views
    val tvFullNameContact : TextView = itemview.findViewById(R.id.tvFullNameContact)
    val tvPhoneContact : TextView = itemview.findViewById(R.id.tvPhoneContact)
}