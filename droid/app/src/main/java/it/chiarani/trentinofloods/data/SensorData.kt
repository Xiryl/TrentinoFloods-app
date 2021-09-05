package it.chiarani.trentinofloods.data

data class SensorData (
	val type : String,
	val name : String,
	val crs : Crs,
	val features : MutableList<Features>
)