package it.chiarani.trentinofloods.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.adapters.FloodsDataAdapter
import it.chiarani.trentinofloods.data.SensorData
import it.chiarani.trentinofloods.databinding.FragmentAllertsBinding
import it.chiarani.trentinofloods.databinding.FragmentFavoriteBinding
import it.chiarani.trentinofloods.utils.Constants
import it.chiarani.trentinofloods.utils.CustomDateFormatter
import it.chiarani.trentinofloods.viewModels.FloodsViewModel
import it.chiarani.trentinofloods.viewModels.ProtCivViewModel
import okhttp3.ResponseBody
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


class AllertsFragment : Fragment(R.layout.fragment_allerts) {

    private lateinit var binding: FragmentAllertsBinding
    private val viewModel: ProtCivViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAllertsBinding.bind(view)

        binding.cardSearchIcon.setOnClickListener {
            findNavController().popBackStack()
        }

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