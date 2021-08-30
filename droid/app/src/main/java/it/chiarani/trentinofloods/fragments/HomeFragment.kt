package it.chiarani.trentinofloods.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
    }
}