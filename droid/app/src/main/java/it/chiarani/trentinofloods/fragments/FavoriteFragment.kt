package it.chiarani.trentinofloods.fragments

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.databinding.FragmentFavoriteBinding
import it.chiarani.trentinofloods.utils.Constants


class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var binding: FragmentFavoriteBinding
   // val args: FavoriteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoriteBinding.bind(view)

        binding.cardSearchIcon.setOnClickListener {
            findNavController().popBackStack()
        }

        val bacinoName = requireArguments().getString(Constants.PREF_KEY_PREF_BACINO.toString())
        val stationName = requireArguments().getString(Constants.PREF_KEY_PREF_STATION.toString())

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
}