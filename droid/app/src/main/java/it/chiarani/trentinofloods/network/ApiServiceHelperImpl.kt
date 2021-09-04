package it.chiarani.trentinofloods.network

import Idrometer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiServiceHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiServiceHelper{
    override suspend fun getJsonIdrometer(): Idrometer {
        return apiService.getJsonIdrometer()
    }
}