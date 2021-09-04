package it.chiarani.trentinofloods.data

data class Properties (
	val idsensore : Int,
	val idstazione : Int,
	val stazione : String,
	val strumento : String,
	val grandezza : String,
	val um : String,
	val entepropr : String,
	val quota : Int,
	val gmt : Int,
	val bacino : String,
	val corpo : String,
	val linkdati : String
)