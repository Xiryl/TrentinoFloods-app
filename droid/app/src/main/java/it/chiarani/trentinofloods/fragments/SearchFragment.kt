package it.chiarani.trentinofloods.fragments

import android.graphics.Color
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.data.SensorData
import it.chiarani.trentinofloods.databinding.FragmentSearchBinding
import it.chiarani.trentinofloods.utils.CustomDateFormatter
import it.chiarani.trentinofloods.viewModels.FloodsViewModel
import okhttp3.ResponseBody
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: FloodsViewModel by activityViewModels()

    private val baciniList = mutableListOf<String>()
    private val stazioniList = mutableListOf<String>()
    private lateinit var floodList : SensorData

    private lateinit var lineChart: LineChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        floodList = getJsonFloods()

        initBaciniSpinner()
//
//        lineChart = binding.chart1
//
//        // style and settings

//
//        val lineData: LineData = LineData()
//        lineData.apply {
//            setValueTextColor(Color.WHITE)
//        }
//
//        lineChart.data = lineData
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

    private fun createSet(color: Int, legend: String) : ILineDataSet {
        val set: LineDataSet = LineDataSet(null, legend)
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(color);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set
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

                }

                val idx = 0
                for(flood in floodList.features) {
                    if(flood.properties.stazione == stazioniList[position] && flood.properties.bacino == bacino) {
                        viewModel.getRiverSensorData(flood.properties.idsensore.toString(), "0").observe(viewLifecycleOwner) {
                            val data = extractAPIData(it.body()!!)
                            setGraph(data!!)
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