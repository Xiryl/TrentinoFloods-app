package it.chiarani.trentinofloods.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.databinding.FragmentHomeBinding
import it.chiarani.trentinofloods.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)
    }
}