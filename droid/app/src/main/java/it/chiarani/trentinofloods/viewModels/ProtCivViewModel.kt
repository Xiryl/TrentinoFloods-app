package it.chiarani.trentinofloods.viewModels

import android.provider.DocumentsContract
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import it.chiarani.trentinofloods.repository.FloodsRepository
import it.chiarani.trentinofloods.repository.ProtCivRepository
import kotlinx.coroutines.Dispatchers
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class ProtCivViewModel @Inject constructor(
    var repository: ProtCivRepository
) : ViewModel() {

    fun getAllerts() = liveData(Dispatchers.IO ) {
        emit(
            Jsoup.connect("https://avvisi.protezionecivile.tn.it/elencoavvisi.aspx").get()
        )
    }

}