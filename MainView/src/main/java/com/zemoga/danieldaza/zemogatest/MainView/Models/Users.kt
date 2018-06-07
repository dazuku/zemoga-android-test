package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONArray

class Users : ArrayList<User>() {
    companion object {
        fun fromJsonArray(array : JSONArray) : Users {
            val users = Users()
            for (i: Int in 0..(array.length() - 1)) {
                val user = User.fromJsonObject(array.getJSONObject(i))
                users.add(user)
            }

            return users
        }
    }
}