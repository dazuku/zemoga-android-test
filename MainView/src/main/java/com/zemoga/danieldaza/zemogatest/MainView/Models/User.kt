package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONObject

data class User(
        val id: Int,
        val name: String,
        val email: String,
        val address: Address,
        val phone: String,
        val website: String,
        val company: Company
) {
    companion object {
        fun fromJsonObject(json: JSONObject): User {
            val id = json.getInt("id")
            val name = json.getString("name")
            val email = json.getString("email")
            val address = Address.fromJsonObject(json.getJSONObject("address"))
            val phone = json.getString("phone")
            val website = json.getString("website")
            val company = Company.fromJsonObject(json.getJSONObject("company"))

            return User(id, name, email, address, phone, website, company)
        }
    }
}