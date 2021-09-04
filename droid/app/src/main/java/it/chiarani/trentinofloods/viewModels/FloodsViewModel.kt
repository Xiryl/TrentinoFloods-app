package it.chiarani.trentinofloods.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.chiarani.trentinofloods.api.FloodsApi
import it.chiarani.trentinofloods.data.Idrometer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FloodsViewModel @Inject constructor(
    api: FloodsApi
) : ViewModel() {

    private val idrometerLiveData = MutableLiveData<Idrometer>()

    // exposed field
    val idrometerData : LiveData<Idrometer> = idrometerLiveData

    init {
        viewModelScope.launch {
            val response = api.getIdrometer0h()
            idrometerLiveData.value = response
        }
    }
}