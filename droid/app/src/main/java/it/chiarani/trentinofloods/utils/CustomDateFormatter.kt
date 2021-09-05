package it.chiarani.trentinofloods.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class CustomDateFormatter : ValueFormatter() {
    override fun getPointLabel(entry: Entry): String {
        return "X"
    }

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:ss")
        return formatter.format(Date(value.toLong()))
    }
}