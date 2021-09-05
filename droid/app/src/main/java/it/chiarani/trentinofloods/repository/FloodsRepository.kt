package it.chiarani.trentinofloods.repository

import it.chiarani.trentinofloods.api.FloodsApi
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class FloodsRepository @Inject constructor(
    var api: FloodsApi
) {
    suspend fun getRiverSensorData(sensor: String, arg: String) : Response<ResponseBody>  {
        return api.getRiverSensorData(sensor, arg)
    }
}