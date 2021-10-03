package it.chiarani.trentinofloods.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.databinding.FragmentHelpBinding
import it.chiarani.trentinofloods.databinding.FragmentHomeBinding
import it.chiarani.trentinofloods.databinding.FragmentInfoBinding

class InfoFragment: Fragment(R.layout.fragment_info) {

    lateinit var binding: FragmentInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInfoBinding.bind(view)

        binding.fragmentFaqBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}