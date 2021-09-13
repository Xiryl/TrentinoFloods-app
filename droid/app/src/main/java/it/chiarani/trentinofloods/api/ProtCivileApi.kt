package it.chiarani.trentinofloods.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ProtCivileApi {
    companion object {
        const val BASE_URL = "https://avvisi.protezionecivile.tn.it/"
    }

    @GET("elencoavvisi.aspx")
    suspend fun getAllert(): Response<ResponseBody>
}