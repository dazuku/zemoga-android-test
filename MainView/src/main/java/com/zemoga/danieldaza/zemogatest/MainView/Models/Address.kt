package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONObject

data class Address (val street: String, val suite: String, val city: String, val zipCode: String?, val geo: GeoLocation) {
    companion object {
        fun fromJsonObject(json: JSONObject): Address {
            val street = json.getString("street")
            val suite = json.getString("suite")
            val city = json.getString("city")
            val zipCode = json.getString("zipcode")
            val geo = GeoLocation.fromJsonObject(json.getJSONObject("geo"))

            return Address(street, suite, city, zipCode, geo)
        }
    }
}