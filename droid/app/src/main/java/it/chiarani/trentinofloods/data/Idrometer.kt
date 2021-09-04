package it.chiarani.trentinofloods.data

data class Idrometer (
	val type : String,
	val name : String,
	val crs : Crs,
	val features : List<Features>
)