package it.chiarani.trentinofloods.viewModels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import it.chiarani.trentinofloods.repository.FloodsRepository
import it.chiarani.trentinofloods.repository.ProtCivRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProtCivViewModel @Inject constructor(
    var repository: ProtCivRepository
) : ViewModel() {

    fun getRiverSensorData() = liveData(Dispatchers.IO ) {
        emit(
            repository.getAllerts()
        )
    }

}