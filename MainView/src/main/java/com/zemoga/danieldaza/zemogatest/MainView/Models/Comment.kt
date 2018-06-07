package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONObject
import java.io.Serializable

data class Comment (val postId: Int, val id: Int, val name: String, val email: String, val body: String): Serializable {
    companion object {
        fun fromJsonObject(json: JSONObject): Comment {
            val postId = json.getInt("postId")
            val id = json.getInt("id")
            val name = json.getString("name")
            val email = json.getString("email")
            val body = json.getString("body")

            return Comment(postId, id, name, email, body)
        }
    }
}