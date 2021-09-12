package it.chiarani.trentinofloods.repository

import it.chiarani.trentinofloods.api.FloodsApi
import it.chiarani.trentinofloods.api.ProtCivileApi
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class ProtCivRepository @Inject constructor(
    var api: ProtCivileApi
) {
    suspend fun getAllerts() : Response<ResponseBody>  {
        return api.getAllert()
    }
}