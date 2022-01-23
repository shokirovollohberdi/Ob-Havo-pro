package uz.shokirov.models

class WeatherModel {
    var lat:Double?=null
    var long:Double?=null

    constructor(lat: Double?, long: Double?) {
        this.lat = lat
        this.long = long
    }

    constructor()

    override fun toString(): String {
        return "WeatherModel(lat=$lat, long=$long)"
    }

}