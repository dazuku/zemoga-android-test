package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONObject

data class Company(val name: String, val catchPhrase: String, val bs: String) {
    companion object {
        fun fromJsonObject(json: JSONObject): Company {
            val name = json.getString("name")
            val catchPhrase = json.getString("catchPhrase")
            val bs = json.getString("bs")

            return Company(name, catchPhrase, bs)
        }
    }
}