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
import it.chiarani.trentinofloods.databinding.FragmentFavoriteBinding
import it.chiarani.trentinofloods.utils.Constants
import it.chiarani.trentinofloods.utils.CustomDateFormatter
import it.chiarani.trentinofloods.viewModels.FloodsViewModel
import okhttp3.ResponseBody
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FloodsViewModel by activityViewModels()

    private lateinit var sp: SharedPreferences
    private lateinit var floodList : SensorData
    private var isCharView = false
    private lateinit var floodsDataAdapter: FloodsDataAdapter
    private var dataRecyclerView = mutableListOf<String>()
    private var graphV = false
    private var currentStation = ""
    private var currentBacino = ""
    private var oldPrefStation = ""
    var stationName: String? = ""
    var bacinoName: String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoriteBinding.bind(view)

        binding.cardSearchIcon.setOnClickListener {
            findNavController().popBackStack()
        }

        bacinoName = requireArguments().getString(Constants.PREF_KEY_PREF_BACINO.toString())
        stationName = requireArguments().getString(Constants.PREF_KEY_PREF_STATION.toString())

        currentStation = stationName!!
        currentBacino = bacinoName!!

        floodList = getJsonFloods()

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


        binding.btnSwitchGraph.setOnClickListener {
            initGraph()
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

        initGraph()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        try {
//        sharedElementEnterTransition = ChangeBounds().apply {
//            duration = 750
//        }
//        sharedElementReturnTransition = ChangeBounds().apply {
//            duration = 750
//        }}
//        catch (ex: Exception) {
//
//        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initGraph() {
        for(flood in floodList.features) {
            if (flood.properties.idsensore.toString() == currentStation && flood.properties.bacino == currentBacino) {
                if(flood.properties.linkdati.contains("Sensore2=NULL")) {
                    Toast.makeText(requireContext(), "Unico sensore: LIVELLO", Toast.LENGTH_LONG).show()
                    graphV = true
                }


                binding.cardSearchPrefName.text = "${flood.properties.bacino} - ${flood.properties.stazione}"

                if(graphV) {
                    viewModel.getRiverSensorData(flood.properties.idsensore.toString(), "0")
                        .observe(
                            viewLifecycleOwner
                        ) {
                            val data = extractAPIData(it.body()!!)
                            setGraph(data!!, "#1B7DFB")
                            binding.btnSwitchView.visibility = View.VISIBLE
                            binding.btnSwitchGraph.visibility = View.VISIBLE
                            binding.chart1.visibility = View.VISIBLE
                            dataRecyclerView.clear()
                            dataRecyclerView.addAll(data.reversed())
                            floodsDataAdapter.notifyDataSetChanged()
                        }
                    binding.btnSwitchGraph.text = "Attuale: LIVELLO"
                } else {

                    val idSensor2 = flood.properties.linkdati.split("&")[2].split("=")[1]
                    viewModel.getRiverSensorData(idSensor2, "0")
                        .observe(
                            viewLifecycleOwner
                        ) {
                            val data = extractAPIData(it.body()!!)
                            setGraph(data!!, "#1B7DFB")
                            binding.btnSwitchView.visibility = View.VISIBLE
                            binding.btnSwitchGraph.visibility = View.VISIBLE
                            binding.chart1.visibility = View.VISIBLE
                            dataRecyclerView.clear()
                            dataRecyclerView.addAll(data.reversed())
                            floodsDataAdapter.notifyDataSetChanged()
                        }
                    binding.btnSwitchGraph.text = "Attuale: PORTATA"
                }
                graphV = !graphV
            }
        }
    }
    private fun setGraph(model: List<String>, hexColor: String) {
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
        dataSet.setColor(Color.parseColor(hexColor));
        dataSet.setHighlightEnabled(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f)
        binding.chart1.getXAxis().setValueFormatter(CustomDateFormatter())

        val lineData = LineData(dataSet)
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