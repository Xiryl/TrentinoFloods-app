package it.chiarani.trentinofloods.viewModels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import it.chiarani.trentinofloods.repository.FloodsRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class FloodsViewModel @Inject constructor(
    var repository: FloodsRepository
) : ViewModel() {

    fun getRiverSensorData(sensor: String, arg: String) = liveData(Dispatchers.IO ) {
        emit(
            repository.getRiverSensorData(sensor, arg)
        )
    }

}