package savemenow.es.protecciontotalresk.android.apis.api

import retrofit2.Call
import retrofit2.http.*
import savemenow.es.protecciontotalresk.android.model.modelcountries.CountryInfo

interface CountryService {

    @GET("all")
    fun getAllCountries(): Call<List<CountryInfo>>

}