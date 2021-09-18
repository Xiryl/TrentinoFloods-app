package it.chiarani.trentinofloods.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.databinding.FragmentHelpBinding
import it.chiarani.trentinofloods.databinding.FragmentHomeBinding

class HelpFragment: Fragment(R.layout.fragment_help) {

    lateinit var binding: FragmentHelpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHelpBinding.bind(view)

        binding.fragmentFaqBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}