package it.chiarani.trentinofloods.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.data.Idrometer
import it.chiarani.trentinofloods.databinding.FragmentHomeBinding
import it.chiarani.trentinofloods.databinding.FragmentSearchBinding
import java.util.*

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding

    private val baciniList = mutableListOf<String>()
    private val stazioniList = mutableListOf<String>()
    private lateinit var floodList : Idrometer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)
        floodList = getJsonFloods()

        initBaciniSpinner()
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
            if(flood.properties.bacino == bacino && !stazioniList.contains(flood.properties.stazione)) {
                stazioniList.add(flood.properties.stazione)
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

    fun getJsonFloods() : Idrometer {
        val fileInString: String = requireContext().assets.open("Idrometers.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(fileInString, Idrometer::class.java)
    }
}