package it.chiarani.trentinofloods.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.adapters.FloodsDataAdapter
import it.chiarani.trentinofloods.data.SensorData
import it.chiarani.trentinofloods.databinding.FragmentFavoriteBinding
import it.chiarani.trentinofloods.databinding.FragmentMapBinding
import it.chiarani.trentinofloods.utils.Constants

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMapBinding.bind(view)

        binding.cardSearchIcon.setOnClickListener {
            findNavController().popBackStack()
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 750
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        googleMap.setOnInfoWindowClickListener(this)

        // Add a marker in trento and move the camera
        val trento = LatLng(46.0374189, 11.0243494)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(trento))

        val floodList = getJsonFloods()

        for(flood in floodList.features) {
            if(flood.properties.strumento == "Idrometro") {
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(flood.geometry.coordinates[1], flood.geometry.coordinates[0]))
                        .title(flood.properties.bacino + " - " + flood.properties.stazione)
                        .snippet("Strumento: " + flood.properties.strumento + ", quota: " + flood.properties.quota)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                marker.tag = flood.properties.idsensore.toString() + ";" + flood.properties.bacino
            } else {
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(flood.geometry.coordinates[1], flood.geometry.coordinates[0]))
                        .title(flood.properties.bacino + " - " + flood.properties.stazione)
                        .snippet("Strumento: " + flood.properties.strumento + ", quota: " + flood.properties.quota)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
                marker.tag = flood.properties.idsensore.toString() + ";" + flood.properties.bacino
            }

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

    override fun onInfoWindowClick(p0: Marker) {
        gotoPage(p0)
    }



    private fun gotoPage(p0: Marker) {
        val bundle = Bundle()
        bundle.putString(Constants.PREF_KEY_PREF_BACINO.toString(), p0.tag.toString().split(";")[1])
        bundle.putString(Constants.PREF_KEY_PREF_STATION.toString(), p0.tag.toString().split(";")[0])

        findNavController().navigate(
            R.id.action_mapFragment_to_FavoriteFragment ,
            bundle,
            null,
            null
        )
    }
}