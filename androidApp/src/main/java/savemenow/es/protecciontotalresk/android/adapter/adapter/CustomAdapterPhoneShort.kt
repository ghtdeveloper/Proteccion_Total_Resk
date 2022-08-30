package savemenow.es.protecciontotalresk.android.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.bindings.AdapterViewListContact
import savemenow.es.protecciontotalresk.android.databinding.ListTiemCountryBinding
import savemenow.es.protecciontotalresk.android.model.contacts.Contacts


/**
 ****Created by: Edison Martinez,
 ****Date:23,Tuesday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class CustomAdapterPhoneShort(private val dataset: ArrayList<Contacts>)
    : RecyclerView.Adapter<AdapterViewListContact>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewListContact
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_tiem_contacts,
            parent,false)
        return AdapterViewListContact(view)
    }

    override fun onBindViewHolder(holder: AdapterViewListContact, position: Int) {
        val dataModel = dataset[position]
        //Set Data
        holder.tvFullNameContact.text = dataModel.getName()
        holder.tvPhoneContact.text = dataModel.getNumbers()
    }

    override fun getItemCount() = 2

}