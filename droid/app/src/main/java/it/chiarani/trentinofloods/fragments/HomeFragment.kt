package it.chiarani.trentinofloods.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.data.Idrometer
import it.chiarani.trentinofloods.databinding.FragmentHomeBinding
import it.chiarani.trentinofloods.viewModels.FloodsViewModel

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: FloodsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        binding.clCardSearch.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_homefragment_to_searchfragment)
            hideBottomSheet()
         }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.root.findViewById(R.id.imported_bottom_sheet))
    }



    override fun onResume() {
        super.onResume()
        showBottomSheet()
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