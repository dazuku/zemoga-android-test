package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONObject

data class GeoLocation(val lat: String, val lng: String) {
    companion object {
        fun fromJsonObject(json: JSONObject): GeoLocation {
            val lat = json.getString("lat")
            val lng = json.getString("lng")

            return GeoLocation(lat, lng)
        }
    }
}