package savemenow.es.protecciontotalresk.android.view.emergency.section.main

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.toObjects
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.adapter.MarkerInfoWindowAdapter
import savemenow.es.protecciontotalresk.android.apis.api.WeatherService
import savemenow.es.protecciontotalresk.android.apis.builder.ServiceBuilderWeather
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.databinding.ActivityMainBinding
import savemenow.es.protecciontotalresk.android.model.modelweather.Weather
import savemenow.es.protecciontotalresk.android.model.modelweather.WeatherInfo
import savemenow.es.protecciontotalresk.android.model.reportemergency.EmergencyInfo
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl
import savemenow.es.protecciontotalresk.android.util.BitmapHelper
import savemenow.es.protecciontotalresk.android.util.DateTimeStamp
import savemenow.es.protecciontotalresk.android.view.emergency.section.settings.home.SettingsMain
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), Contract.IMainMenuView, OnMapReadyCallback {

    //Views
    private lateinit var binding: ActivityMainBinding
    //Objects
    private lateinit var result: ArrayList<Weather>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var presenterFirebase : FirebasePresenterCompl
    private lateinit var dialogCallEm: BottomDialogCallEm
    private var addressPlace =""
    private var latitudeGet =0.0
    private var longitudeGet =0.0
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenterFirebase = FirebasePresenterCompl()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        dialogCallEm = BottomDialogCallEm(this)
        //Init
        initViews()

    }

    override fun initViews() {
        //Config Toolbar
        this.setSupportActionBar(binding.toolbarMainMenu)
        this.supportActionBar!!.title = "Bienvenido ${intent.getStringExtra("fullName")}"
        this.supportActionBar!!.subtitle = getString(R.string.text_info_welcome)
        //Listener
        onClickBottomNav()

        mapFragment  = supportFragmentManager.findFragmentById(R.id.map_fragment)
              as SupportMapFragment

        binding.layoutCardviewCallEmergency.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",intent.getStringExtra("id"))//ID
            bundle.putString("addressPlace",addressPlace)
            bundle.putDouble("latitudeGet",latitudeGet)
            bundle.putDouble("longitudeGet",longitudeGet)
            dialogCallEm.arguments = bundle
            dialogCallEm.show(supportFragmentManager,"Show")
        }

        binding.btnMyLocation.setOnClickListener {
            getViewMaps()
        }

        binding.btnEmergencies.setOnClickListener {
            mapFragment.getMapAsync(this)
        }
        //Maps
        getViewMaps()
    }




    fun getViewMaps()
    {
        var geocoder : Geocoder?

        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location != null) {
                Log.d("TAG","LAST LOCATION Latitude ${location.latitude}")
                latitudeGet =location.latitude
               // Log.d("TAG","Latitude GET $latitudeGet")
            }
            if (location != null) {
                Log.d("TAG","LAST LOCATION Longitude ${location.longitude}")
                longitudeGet = location.longitude
            }
            geocoder = Geocoder(this, Locale.getDefault())
            val listAdress : List<Address> =geocoder!!.getFromLocation(location!!.latitude,
                location!!.longitude,1)
            val obj : Address = listAdress[0]

           // Log.d("TAG","ADDRESS PLACE${obj.getAddressLine(0)}")
            addressPlace=obj.getAddressLine(0)

            binding.tvPlaceName.text = obj.getAddressLine(0)
            //Fetch Data WS
            getDataWeather(location.latitude,location.longitude)
            //Maps
            lifecycleScope.launchWhenCreated {
                val mapFragment: SupportMapFragment? =
                    supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
                val googleMap: GoogleMap? = mapFragment?.awaitMap()
                googleMap!!.setInfoWindowAdapter(MarkerInfoWindowAdapter(this@MainActivity))
                //addMarkers(googleMap)
                val placeMark = LatLng(location.latitude, location.longitude)
                val marker  = googleMap.addMarker {
                    position(placeMark)
                    title(addressPlace)
                    position(placeMark)
                    icon(iconPeople)
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeMark, 15F))
                googleMap.uiSettings.isZoomControlsEnabled = true
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
                 // Log.d("TAG","CALL WS SUCCESS")
                 // Log.d("TAG","COUNTRY CLIMATE LOCATION ${response.body()!!.name}")
                 // Log.d("TAG","TEMPERATURE ${response.body()!!.temperature.temp.toInt()}°C")

                  for (details in result)
                  {
                      binding.tvDescWeather.text = details.description
                      //Log.d("TAG","ICON WS CODE ${details.icon}")
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

    private val iconPeople: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.status_bar)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_emoji_people_64, color)
    }

    private val iconEmergency: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.colorPrimary)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_emergency_share_48, color)
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        mMap = googleMap

        presenterFirebase.getReportEmergency().addSnapshotListener{ snapshot, e ->
            if(e!= null)
            {
                Log.e("TAG","Listen failed $e")
                return@addSnapshotListener
            }
            if(snapshot!=null && !snapshot.isEmpty)
            {
                val emergencyInfo = snapshot.toObjects<EmergencyInfo>()
                for (value in emergencyInfo)
                {
                    val geopoint : GeoPoint? = value.geoPoint
                    val latLng : LatLng = LatLng(geopoint!!.latitude, geopoint.longitude)
                    mMap.addMarker{
                        position(latLng).title(value.type).icon(iconEmergency)
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15F))
                    // Log.d("TAG","DATA ${emergencyInfo[0].geoPoint}")
                    googleMap.uiSettings.isZoomControlsEnabled = true
                }
            }else
            {
                Log.e("TAG","Current data: null")
            }
        }
    }

}
