package it.chiarani.trentinofloods.network

import Idrometer

interface ApiServiceHelper {
    suspend fun getJsonIdrometer(): Idrometer
}