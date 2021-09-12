package it.chiarani.trentinofloods.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import java.util.*

interface ProtCivileApi {
    companion object {
        const val BASE_URL = "https://avvisi.protezionecivile.tn.it/"
    }

    @GET("elencoavvisi.aspx")
    fun getAllert(): Response<ResponseBody>
}