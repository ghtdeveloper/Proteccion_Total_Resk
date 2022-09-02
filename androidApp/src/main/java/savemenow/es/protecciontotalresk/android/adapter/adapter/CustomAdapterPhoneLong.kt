package savemenow.es.protecciontotalresk.android.adapter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.bindings.AdapterViewListContact
import savemenow.es.protecciontotalresk.android.model.contacts.Contacts


/**
 ****Created by: Edison Martinez,
 ****Date:23,Tuesday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class CustomAdapterPhoneLong(
    private val dataset: ArrayList<Contacts>,
    private val listener: ItemClickListenerContacts)
    : RecyclerView.Adapter<AdapterViewListContact>()
{
    //Variable
    private var seletectName : String = ""
    private var selectPhone : String = ""

    companion object {
        var mClickListener: ItemClickListenerContacts? = null
    }

    interface ItemClickListenerContacts
    {
        fun clickRow(post:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewListContact
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_tiem_contacts,
            parent,false)
        return AdapterViewListContact(view)
    }

    override fun onBindViewHolder(holder: AdapterViewListContact, position: Int) {
        mClickListener = listener
        val dataModel = dataset[position]
        //Set Data
        holder.tvFullNameContact.text = dataModel.getName()
        holder.tvPhoneContact.text = dataModel.getNumbers()

        holder.cardView.setOnClickListener {
            setContactName(dataModel.getName())
            setPhoneNumber(dataModel.getNumbers())
            mClickListener!!.clickRow(position)
        }
    }

    override fun getItemCount() = dataset.size

    fun setContactName(name:String)
    {
        this.seletectName = name
    }

    fun setPhoneNumber(phone : String)
    {
        this.selectPhone = phone
    }

    fun getContactName(): String
    {
        return seletectName
    }

    fun getPhoneNumber() : String
    {
        return selectPhone
    }

}