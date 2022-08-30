package savemenow.es.protecciontotalresk.android.adapter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.bindings.AdapterViewListCountries
import savemenow.es.protecciontotalresk.android.model.modelcountries.CountryInfo


/**
 ****Created by: Edison Martinez,
 ****Date:28,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class AdapterListCountries(private val countryInfo: ArrayList<CountryInfo>,
                           private val context: Context,
                           private val listener: ItemClickListener) : RecyclerView.
Adapter<AdapterViewListCountries>(), Filterable
{
    private val searchableList = ArrayList<CountryInfo>()
    private val originaList = ArrayList(searchableList)
    private var selectedCountry : String = ""
    private var selectedFlagCountry : String = ""

    companion object {
        var mClickListener: ItemClickListener? = null
    }


     interface ItemClickListener
    {
        fun clickRow(post:Int)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<CountryInfo>?)
    {
        this.searchableList.clear()
        items?.let {
            this.searchableList.addAll(it)
            originaList.addAll(it)
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewListCountries {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_tiem_country,parent,
        false)
        return AdapterViewListCountries(view)
    }

    override fun onBindViewHolder(holder: AdapterViewListCountries, position: Int)
    {
        mClickListener = listener
        val dataModel = countryInfo[position]
        holder.tvCountryName.text = dataModel.name?.official
        Glide.with(context)
            .load(dataModel.flags?.png)
            .into(holder.imgFlagCountry)
        holder.cardViewListCountrie.setOnClickListener {
            setCountryName(dataModel.name?.official.toString())
            setFlagCountry(dataModel.flags?.png.toString())
        // mItemClickListener.onClickPos(position)
            mClickListener!!.clickRow(position)
        }

    }

    override fun getItemCount(): Int = countryInfo.size

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }


    fun setCountryName(name:String)
    {
        this.selectedCountry = name
    }

    fun setFlagCountry(flagUri : String)
    {
        this.selectedFlagCountry = flagUri
    }

    fun getCountryName() : String
    {
        return selectedCountry
    }

    fun getFlagUri() : String
    {
        return selectedFlagCountry
    }






}