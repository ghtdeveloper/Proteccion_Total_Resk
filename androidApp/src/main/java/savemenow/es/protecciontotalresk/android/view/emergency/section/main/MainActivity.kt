package savemenow.es.protecciontotalresk.android.view.emergency.section.main

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.apis.api.WeatherService
import savemenow.es.protecciontotalresk.android.apis.builder.ServiceBuilderWeather
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.databinding.ActivityMainBinding
import savemenow.es.protecciontotalresk.android.model.modelweather.Weather
import savemenow.es.protecciontotalresk.android.model.modelweather.WeatherInfo
import savemenow.es.protecciontotalresk.android.util.DateTimeStamp
import savemenow.es.protecciontotalresk.android.view.emergency.section.settings.home.SettingsMain
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), Contract.IMainMenuView {

    //Views
    private lateinit var binding: ActivityMainBinding
    //Objects
    private lateinit var result: ArrayList<Weather>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //Init
        initViews()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun initViews() {
        //Config Toolbar
        this.setSupportActionBar(binding.toolbarMainMenu)
        this.supportActionBar!!.title = "Bienvenido ${intent.getStringExtra("fullName")}"
        this.supportActionBar!!.subtitle = getString(R.string.text_info_welcome)
        //Listener
        onClickBottomNav()
        //Mpas
        getViewMaps()
    }

    fun getViewMaps()
    {
        var geocoder : Geocoder?

        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location != null) {
                Log.d("TAG","LAST LOCATION Latitude ${location.latitude}")
            }
            if (location != null) {
                Log.d("TAG","LAST LOCATION Longitude ${location.longitude}")

            }
            geocoder = Geocoder(this, Locale.getDefault())
            val listAdress : List<Address> =geocoder!!.getFromLocation(location!!.latitude,
                location.longitude,1)
            val obj : Address = listAdress[0]

            Log.d("TAG","ADDRESS PLACE${obj.getAddressLine(0)}")
            binding.tvPlaceName.text = obj.getAddressLine(0)
            //Fetch Data WS
            getDataWeather(location.latitude,location.longitude)
            //Maps
            lifecycleScope.launchWhenCreated {
                val mapFragment: SupportMapFragment? =
                    supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
                val googleMap: GoogleMap? = mapFragment?.awaitMap()
                val placeMark = LatLng(location.latitude, location.longitude)
                val marker  = googleMap!!.addMarker {
                    position(placeMark)
                    title("Location")
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeMark, 15F))

            }
        }

    }

    override fun onClickBottomNav() {
      binding.bottomNavMainMenu.setOnItemReselectedListener {
           when(it.itemId)
           {
               R.id.home_menu -> {}

               R.id.device_menu ->
               {
                   Toast.makeText(this,"HOME DEVICE",Toast.LENGTH_SHORT).show()
               }

               R.id.settings_menu ->
               {
                  showSettingsActivity(intent.getStringExtra("id")!!,
                      intent.getStringExtra("email")!!)
               }
           }
       }
    }
    /*
        Pass latitud  Longitud (get from Google Maps)
     */
    fun getDataWeather(lat:Double,long:Double)
    {
        val retrofit = ServiceBuilderWeather.buildService(WeatherService ::class.java)
        retrofit.getInfoWeather(lat = lat, lon =long,
                                appID="585fcc9a919a55cd07ae6f5b8e68a9d8",
                                lang = "sp", units = "metric" ).
        enqueue(object :Callback<WeatherInfo>
        {
            override fun onResponse(
                call: Call<WeatherInfo>,
                response: Response<WeatherInfo>)
            {
              if(response.body()!!.cod == 200)
              {
                  result = response.body()!!.getArrayWeather() as ArrayList<Weather>
                  Log.d("TAG","CALL WS SUCCESS")
                  Log.d("TAG","COUNTRY CLIMATE LOCATION ${response.body()!!.name}")
                  Log.d("TAG","TEMPERATURE ${response.body()!!.temperature.temp.toInt()}°C")

                  for (details in result)
                  {
                      binding.tvDescWeather.text = details.description
                      Log.d("TAG","ICON WS CODE ${details.icon}")
                      Glide.with(this@MainActivity).
                      load("https://openweathermap.org/img/wn/"+details.icon+"@4x.png")
                          .into(binding.imgIconWeather)
                  }

                  binding.tvNameWeatherWs.text = response.body()!!.name
                  binding.tvDateWs.text = DateTimeStamp().getCurrentDate()
                  binding.tvTempWs.text = response.body()!!.temperature.temp.toInt().toString()+""+"°C"

              }
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable)
            {
                Log.e("TAG","ERROR CALL WS $t")
            }

        })

    }

    override fun showSettingsActivity(uid: String, email: String) {
      val intent = Intent(this, SettingsMain::class.java)
        intent.putExtra("id",uid)
        intent.putExtra("email",email)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
