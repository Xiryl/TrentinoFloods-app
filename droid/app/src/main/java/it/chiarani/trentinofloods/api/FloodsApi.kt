package it.chiarani.trentinofloods.api

import it.chiarani.trentinofloods.data.SensorData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FloodsApi {
    companion object {
        const val BASE_URL = "https://www.floods.it"
    }

    @GET("/public/SensorData/Pluviometri0h_level_pub.json")
    suspend fun getIdrometer0h(): SensorData

    @GET("/public/download.php")
    suspend fun getRiverSensorData(
        @Query("Sensore") sensor: String?,
        @Query("Argomento") argument: String?
    ): Response<ResponseBody>
}