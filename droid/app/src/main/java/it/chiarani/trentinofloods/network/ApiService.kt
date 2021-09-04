package it.chiarani.trentinofloods.network

import Idrometer
import it.chiarani.trentinofloods.utils.RestConfig
import retrofit2.http.GET

interface ApiService {
    @GET(RestConfig.IDROMETER)
    suspend fun getJsonIdrometer(): Idrometer
}