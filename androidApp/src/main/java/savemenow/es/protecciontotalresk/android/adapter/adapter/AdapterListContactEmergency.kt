package savemenow.es.protecciontotalresk.android.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.bindings.AdapterViewListContact
import savemenow.es.protecciontotalresk.android.model.contacts.EmergencyContacts


/**
 ****Created by: Edison Martinez,
 ****Date:01,Thursday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class AdapterListContactEmergency(options :FirestoreRecyclerOptions<EmergencyContacts>,
                                  private val listener:ItemClickListener) :
    FirestoreRecyclerAdapter<EmergencyContacts, AdapterViewListContact>(options)
{
    private var seletectPhone : String = ""

    companion object {
        var mClickListener: ItemClickListener? = null
    }

    interface ItemClickListener
    {
        fun clickRow(post:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewListContact
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_tiem_contacts,
            parent,false)
        return AdapterViewListContact(view)
    }

    override fun onBindViewHolder(holder: AdapterViewListContact, position: Int,
                                  model: EmergencyContacts)
    {
        mClickListener = listener
        holder.tvFullNameContact.text = model.name
        holder.cardView.setOnClickListener {
            setPhoneNummber(phone = model.phone.toString())
            mClickListener!!.clickRow(position)
        }
    }

    fun setPhoneNummber(phone : String)
    {
        this.seletectPhone = phone
    }

    fun getPhoneNumber() : String
    {
        return seletectPhone
    }

}