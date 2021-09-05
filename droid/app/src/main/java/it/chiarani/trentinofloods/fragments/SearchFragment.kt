package it.chiarani.trentinofloods.fragments

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.data.SensorData
import it.chiarani.trentinofloods.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding

    private val baciniList = mutableListOf<String>()
    private val stazioniList = mutableListOf<String>()
    private lateinit var floodList : SensorData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        floodList = getJsonFloods()

        initBaciniSpinner()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementReturnTransition= ChangeBounds().apply {
            duration = 750
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBaciniSpinner() {
        for (flood in floodList.features) {
            if(!baciniList.contains(flood.properties.bacino)) {
                baciniList.add(flood.properties.bacino)
            }
        }

        baciniList.sort()
        binding.spinnerBacini.item = baciniList.toList()

        binding.spinnerBacini.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                initStazioniSpinnder(baciniList[position])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }

    fun initStazioniSpinnder(bacino: String) {
        stazioniList.clear()
        for (flood in floodList.features) {
            if(flood.properties.bacino == bacino) {
                if(flood.properties.strumento == "Pluviometro") {
                    stazioniList.add(flood.properties.stazione + " (Pluviometro)")
                } else {
                    stazioniList.add(flood.properties.stazione)
                }
            }
        }

        stazioniList.sort()
        binding.spinnerStazione.item = stazioniList.toList()

        binding.spinnerStazione.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                // TODO: Download csv data
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }

    fun getJsonFloods() : SensorData {
        val fileInString: String = requireContext().assets.open("Pluviometers.json").bufferedReader().use { it.readText() }
        val fileIdroInString: String = requireContext().assets.open("Idrometers.json").bufferedReader().use { it.readText() }
        val pluviometerData = Gson().fromJson(fileInString, SensorData::class.java)
        val idrometerData = Gson().fromJson(fileIdroInString, SensorData::class.java)
        idrometerData.features.addAll(pluviometerData.features)
        return idrometerData
    }
}