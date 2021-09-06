package it.chiarani.trentinofloods.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import it.chiarani.trentinofloods.databinding.FragmentSearchBinding
import it.chiarani.trentinofloods.utils.Constants
import it.chiarani.trentinofloods.utils.CustomDateFormatter
import it.chiarani.trentinofloods.viewModels.FloodsViewModel
import okhttp3.ResponseBody
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var sp: SharedPreferences
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: FloodsViewModel by activityViewModels()

    private val baciniList = mutableListOf<String>()
    private val stazioniList = mutableListOf<String>()
    private lateinit var floodList : SensorData
    private var isCharView = false
    private lateinit var floodsDataAdapter: FloodsDataAdapter
    private var dataRecyclerView = mutableListOf<String>()
    private var currentStation = ""
    private var oldPrefStation = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        floodList = getJsonFloods()

        initBaciniSpinner()

        binding.cardSearchIcon.setOnClickListener {
            findNavController().popBackStack()
        }

        sp = PreferenceManager.getDefaultSharedPreferences(context)
        oldPrefStation = sp.getString(Constants.PREF_KEY_PREF_STATION.toString(), "")!!

        binding.btnSwitchView.setOnClickListener {
            if(isCharView) {
                binding.chart1.visibility = View.VISIBLE
                binding.recyclerViewFloodData.visibility = View.GONE
                binding.btnSwitchView.text = "Vista Tabella"
            } else {
                binding.chart1.visibility = View.GONE
                binding.recyclerViewFloodData.visibility = View.VISIBLE
                binding.btnSwitchView.text = "Vista Grafico"
            }

            isCharView = !isCharView
        }

        floodsDataAdapter = FloodsDataAdapter(dataRecyclerView)

        binding.recyclerViewFloodData.apply {
            adapter = floodsDataAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        binding.pref.setOnClickListener {
            if(currentStation.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Seleziona prima una stazione!", Toast.LENGTH_LONG)
                    .show()
            } else {
                binding.pref.setImageResource(R.drawable.ic_star_full)
                sp.edit().putString(Constants.PREF_KEY_PREF_STATION.toString(), currentStation).apply()
                Toast.makeText(requireContext(), "Salvato come preferito!", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setGraph(model: List<String>) {
        val entries: MutableList<Entry> = ArrayList()
        for (line in model) {
            // turn your data into Entry objects
            val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            try {
                val tmp = line.split(";").toTypedArray()[0] + ":00"
                val date = inputFormat.parse(tmp) as Date
                val time = date.time
                val value = line.split(";").toTypedArray()[1].replace(",", ".")
                entries.add(Entry(time.toFloat(), value.toFloat()))
            } catch (ex: Exception) {
                val dx = 1
            }
        }
        val dataSet = LineDataSet(entries, "Grafico Andamento")
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setLineWidth(3f);
        dataSet.setColor(Color.parseColor("#1B7DFB"));
        dataSet.setHighlightEnabled(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);

        val lineData = LineData(dataSet)
        binding.chart1.getXAxis().setValueFormatter(CustomDateFormatter())

        binding.chart1.getDescription().setText("Grafico andamento")
        binding.chart1.getAxisLeft().setDrawGridLines(false)
        binding.chart1.getXAxis().setDrawGridLines(false)
        binding.chart1.getLegend().setEnabled(false)
        binding.chart1.getXAxis().setGranularity(2000f)
        binding.chart1.getXAxis().setLabelRotationAngle(-45f)
        binding.chart1.setData(lineData)
        binding.chart1.invalidate() // refresh
        binding.chart1.refreshDrawableState()

        binding.chart1.apply {
            description.isEnabled = false
            isAutoScaleMinMaxEnabled = true
            description.text = ""
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setDrawGridBackground(false)
            setPinchZoom(true)
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(true)
            axisRight.setDrawGridLines(false)
            setBackgroundColor(Color.WHITE)
            legend.isEnabled = false
            xAxis.setDrawLabels(true)
            axisLeft.setDrawLabels(true)
            axisRight.setDrawLabels(false)
            axisLeft.setDrawAxisLine(true)
            axisRight.setDrawAxisLine(false)
            xAxis.setDrawAxisLine(false)
            setDrawBorders(false)
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
        sharedElementReturnTransition= ChangeBounds().apply {
            duration = 750
        }
        return super.onCreateView(inflater, container, savedInstanceState)
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
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                initStazioniSpinnder(baciniList[position])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }

    fun initStazioniSpinnder(bacino: String) {
        stazioniList.clear()
        for (flood in floodList.features) {
            if(flood.properties.bacino == bacino) {
                if(flood.properties.strumento == "Pluviometro") {
                    stazioniList.add(flood.properties.stazione + " (Pluviometro)")
                } else {
                    stazioniList.add(flood.properties.stazione)
                }
            }
        }

        stazioniList.sort()
        binding.spinnerStazione.item = stazioniList.toList()

        binding.spinnerStazione.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if(stazioniList[position].contains("Pluviometro")) {
                    // TODO
                }

                for(flood in floodList.features) {
                    if(flood.properties.stazione == stazioniList[position] && flood.properties.bacino == bacino) {
                        currentStation = flood.properties.idsensore.toString()

                        if(oldPrefStation == currentStation) {
                            binding.pref.setImageResource(R.drawable.ic_star_full)
                        } else {
                            binding.pref.setImageResource(R.drawable.ic_star)
                        }

                        viewModel.getRiverSensorData(flood.properties.idsensore.toString(), "0").observe(viewLifecycleOwner) {
                            val data = extractAPIData(it.body()!!)
                            setGraph(data!!)
                            binding.btnSwitchView.visibility = View.VISIBLE
                            binding.chart1.visibility = View.VISIBLE
                            binding.pref.visibility = View.VISIBLE
                            dataRecyclerView.addAll(data)
                            floodsDataAdapter.notifyDataSetChanged()
                        }

                        return
                    }
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }

    private fun extractAPIData(model: ResponseBody): List<String>? {
        val inputStream = model.byteStream()
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line = ""
        val data: MutableList<String> = ArrayList()
        return try {
            reader.readLine()
            reader.readLine()
            reader.readLine()
            reader.readLine()
            reader.readLine()
            while (reader.readLine().also { line = it } != null) {
                data.add(line)
            }
            data
        } catch (ex: Exception) {
            data
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
}