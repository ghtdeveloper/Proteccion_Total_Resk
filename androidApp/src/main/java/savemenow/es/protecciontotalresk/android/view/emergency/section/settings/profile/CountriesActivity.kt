package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.adapter.AdapterListCountries
import savemenow.es.protecciontotalresk.android.apis.api.CountryService
import savemenow.es.protecciontotalresk.android.apis.builder.ServiceBuilderCountry
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.databinding.ActivityCountriesBinding
import savemenow.es.protecciontotalresk.android.model.modelcountries.CountryInfo

@AndroidEntryPoint
class CountriesActivity : AppCompatActivity(), Contract.IViewCountries {

    //Views
    private lateinit var binding: ActivityCountriesBinding
    //Objects
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapterListCountries: AdapterListCountries
    private lateinit var dataListCountries : ArrayList<CountryInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Test ViewModel
        //Init
         initViews()
        //Test ViewModel
    }

    override fun initViews()
    {
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclewViewCountries.layoutManager = linearLayoutManager
        //Config Toolbar
        setSupportActionBar(binding.toolbarCountries)
        this.supportActionBar!!.title = getString(R.string.text_list_countries)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        getDataCountries()
    }


     fun getDataCountries()
    {
        dataListCountries = ArrayList()
        val retrofit = ServiceBuilderCountry.buildService(CountryService:: class.java)
        retrofit.getAllCountries().enqueue(object : Callback<List<CountryInfo>>
        {
            override fun onFailure(call: Call<List<CountryInfo>>, t: Throwable) {
              Log.e("TAG","ERROR CALL WS $t")
            }

            override fun onResponse(call: Call<List<CountryInfo>>, response:
            Response<List<CountryInfo>>
            ) {
                if(response.body()?.isEmpty() == true)
                {
                    Log.d("TAG","DATA WS IS EMPTY")

                }else
                {
                    Log.d("TAG","DATA WS IS FULL")
                    dataListCountries.addAll(response.body()!!)
                    adapterListCountries = AdapterListCountries(dataListCountries, context = baseContext
                        ,object : AdapterListCountries.ItemClickListener
                        {
                            override fun clickRow(post: Int) {
                                val intent = Intent(this@CountriesActivity,
                                    ProfileEditActivity::class.java)
                                intent.putExtra("edit_country",true)
                                intent.putExtra("selected_country_name",adapterListCountries.
                                getCountryName())
                                intent.putExtra("selected_flag_country",adapterListCountries.
                                getFlagUri())
                                startActivity(intent)
                            }

                        })
                    binding.recyclewViewCountries.adapter = adapterListCountries
                }
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}