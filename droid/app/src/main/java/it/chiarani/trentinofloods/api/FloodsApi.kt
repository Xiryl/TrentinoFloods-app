package it.chiarani.trentinofloods.api

import it.chiarani.trentinofloods.data.Idrometer
import retrofit2.http.GET

interface FloodsApi {
    companion object {
        const val BASE_URL = "https://www.floods.it"
    }

    @GET("/public/SensorData/Pluviometri0h_level_pub.json")
    suspend fun getIdrometer0h(): Idrometer

}