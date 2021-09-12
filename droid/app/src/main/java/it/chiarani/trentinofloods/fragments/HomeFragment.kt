package it.chiarani.trentinofloods.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.databinding.FragmentHomeBinding
import it.chiarani.trentinofloods.utils.Constants
import it.chiarani.trentinofloods.viewModels.FloodsViewModel
import okhttp3.ResponseBody
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: FloodsViewModel by activityViewModels()
    private lateinit var sp: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        sp = PreferenceManager.getDefaultSharedPreferences(context)

        val extra = FragmentNavigator.Extras.Builder()
            .addSharedElement(binding.clCardSearch, "cl_card_search_transition")
            .build()

        binding.clCardSearch.setOnClickListener { view ->
            hideBottomSheet()
            view.findNavController().navigate(
                R.id.action_homefragment_to_searchfragment,
                null,
                null,
                extra
            )
        }

        val extra2 = FragmentNavigator.Extras.Builder()
            .addSharedElement(binding.clCardRecent, "cl_card_favorite_transition")
            .build()

        val stationName = sp.getString(Constants.PREF_KEY_PREF_STATION.toString(), "")
        val bacinoName = sp.getString(Constants.PREF_KEY_PREF_BACINO.toString(), "")

        val bundle = Bundle()
        bundle.putString(Constants.PREF_KEY_PREF_BACINO.toString(), bacinoName)
        bundle.putString(Constants.PREF_KEY_PREF_STATION.toString(), stationName)

        binding.clCardRecent.setOnClickListener { view ->
            hideBottomSheet()
            view.findNavController().navigate(
                R.id.action_HomeFragment_to_favoriteFragment,
                bundle,
                null,
                extra2
            )
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.root.findViewById(R.id.imported_bottom_sheet))
    }

    override fun onResume() {
        super.onResume()
        sp = PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showBottomSheet() {
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_DRAGGING
    }


}